package ind.xwm.imooc.concurrence.aio.future.wrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.Future;

public class ChannelWrapper implements AIOWrapper {
    private static Logger logger = LogManager.getLogger(ChannelWrapper.class);
    private static CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

    private AsynchronousSocketChannel channel;

    private ByteBuffer bufferRead = ByteBuffer.allocateDirect(128 * 1024);
    private Future<Integer> futureRead = null;
    private String msgRead = null;
    private final Object lockRead = new Object();

    public ChannelWrapper(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public boolean isReadDone() {
        synchronized (lockRead) {
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
                    if (futureRead.get() > 0) {
                        bufferRead.flip();
                        CharBuffer charBuffer = decoder.decode(bufferRead);
                        charBuffer.flip();
                        msgRead = charBuffer.toString();
                        bufferRead.compact();
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
                return msgRead;
            } catch (Exception e) {
                logger.info("ChannelWrapper:异常{}-", e.getMessage(), e);
                close();
            } finally {
                lockRead.notify();
            }

        }
        return null;
    }

    @Override
    public void writeCall() {

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
