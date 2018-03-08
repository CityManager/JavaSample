package ind.xwm.imooc.concurrence.aio.future.wrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * AsynchronousSocketChannel的包装类，
 * 提供异步方法readAsync给到 handler调用
 * 提供readCall 和 writeCall 给到 ReadWorker 和 WriteWorker 调用
 */
public class ChannelWrapper implements AIOWrapper {
    private static Logger logger = LogManager.getLogger(ChannelWrapper.class);
    private static CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

    private AsynchronousSocketChannel channel;

    private ByteBuffer bufferRead = ByteBuffer.allocateDirect(128 * 1024);
    private CharBuffer charBufferRead = CharBuffer.allocate(32 * 1024);
    private Future<Integer> futureRead = null;
    private String msgRead = null;
    private final Object lockRead = new Object();

    private ByteBuffer bufferWrite = ByteBuffer.allocateDirect(128 * 1024);
    private Future<Integer> futureWrite = null;
    private ConcurrentLinkedQueue<String> writeQueue = new ConcurrentLinkedQueue<>();

    public ChannelWrapper(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    /**
     * 这里使用 synchronized 存在弊端：
     * 1. 主要的读流程 isReadDone --> get --> read
     * 2. 假设有两个线程A、B，
     * A执行 isReadDone 加锁，返回后，释放锁，
     * 然后get 数据，在要 read 前被系统线程调度挂起，转去执行B
     * <p>
     * B线程 isReadDone --> get --> read 调完后被挂起，回到A线程
     * <p>
     * A线程 read
     * （如果B线程的异步read操作还没有完成，则没事， 因为这里还会判断是否可执行read）
     * （但是，若B线程异步read已经完成，数据读取了， A再 read ，则B线程 read的数据有可能被吞掉）
     * <p>
     * 优化：
     * 1. 使用 Lock 对象 进行 isReadDone --> get --> read 三个方法串联加锁
     * 2. 将 isReadDone --> get --> read 统一成一个方法
     * -- 这个可能不行， get是阻塞方法(可以非阻塞化，没数据就返回null嘛)
     *
     * @return 是否读完成
     */
    @Override
    public boolean isReadDone() {
        synchronized (lockRead) {  // 这里是不是可以去掉这个加锁？
            return futureRead == null;
        }
    }

    @Override
    public void read() {  // 被 handler线程调用
        synchronized (lockRead) {
            if (channel.isOpen() && futureRead == null) {
                futureRead = channel.read(bufferRead);
            }
            lockRead.notify();  // 应该随意 notify 都可以
        }
    }

    /**
     * 前传： 设计： 假设客户端传输的数据大于 Buffer的容量，所以分多次进行异步读取
     * 问题：  当发起 channel.read(bufferRead) 后，无论本次客户端的数据是否已经发送完数据
     * 服务端都已经处于异步 等待读 或者 读的过程，根本就没有进行 futureRead = null; 机会
     * <p>
     * 现在： 设计： 增加 Buffer 容量(128 * 1024)，每次只读取一次， 客户端限制发送的数据长度
     */
    @Override
    public void readCall() {  // 被写线程调用
        synchronized (lockRead) {
            try {
                if (!this.isClosed() && futureRead != null && futureRead.isDone()) {
                    if (futureRead.get() != -1) {
                        bufferRead.flip();

                        decoder.decode(bufferRead, charBufferRead, false);
                        charBufferRead.flip();
                        msgRead = charBufferRead.toString();
                        charBufferRead.clear();
                        logger.info("readCall-" + msgRead);
                        bufferRead.clear();
                    } else { // 链接已经断开
                        close();
                    }
                    futureRead = null;
                }
                lockRead.notify();
            } catch (Exception e) {
                logger.info("ChannelWrapper:异常{}-", e.getMessage(), e);
                close();
            }
        }
    }


    @Override
    public String get() {
        synchronized (lockRead) {
            try {
                if (futureRead != null) {
                    lockRead.wait();
                }
                String msg = msgRead;
                msgRead = "";
                return msg;
            } catch (Exception e) {
                logger.info("ChannelWrapper:异常{}-", e.getMessage(), e);
                close();
            } finally {
                lockRead.notify();
            }
        }
        return null;
    }


    /**
     * 整合上面的 isReadDone -- get -- read 方法
     * handler线程调用
     *
     * @return 返回上次readAsync 调用的结果
     */
    @Override
    public String readAsync() {
        String result = null;
        synchronized (lockRead) {
            if (channel.isOpen() && futureRead == null) {
                result = msgRead;
                logger.info("async read - {}", result);
                futureRead = channel.read(bufferRead);
            }
        }
        return result;
    }


    @Override
    public void write(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            logger.info("writeQueue - {}", msg);
            writeQueue.add(msg);
        }
    }

    @Override
    public void writeCall() { // 已经限定长度了，可以简单来
        boolean isWritable = true;
        try {
            if (isClosed()) {
                isWritable = false;
            } else if (futureWrite != null && !futureWrite.isDone()) {
                isWritable = false;
            } else if (futureWrite != null && futureWrite.isDone() && (futureWrite.get() == -1)) {
                isWritable = false;
                close();
            }
        } catch (Exception e) {
            logger.info("ChannelWrapper:异常{}-", e.getMessage(), e);
            close();
            isWritable = false;
        }
        if(isWritable) {
            String msg = writeQueue.poll();
            if(StringUtils.isNotBlank(msg)) {
                logger.info("writeCall-{}", msg);
                bufferWrite.clear();
                bufferWrite.put(msg.getBytes());
                bufferWrite.flip();
                futureWrite = channel.write(bufferWrite);
            }
        }
    }


    @Override
    public boolean isClosed() {
        return channel == null || !channel.isOpen();
    }


    /**
     * 关闭channel
     */
    private void close() {
        try {
            channel.close();
        } catch (IOException e) {
            logger.info("ChannelWrapper:异常{}-", e.getMessage(), e);
        } finally {
            channel = null;
        }
    }
}
