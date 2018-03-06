package ind.xwm.imooc.concurrence.aio.future.wrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class ChannelWrapper implements AIOWrapper {
    private static Logger logger = LogManager.getLogger(ChannelWrapper.class);

    private AsynchronousSocketChannel channel;

    private ByteBuffer bufferRead = ByteBuffer.allocateDirect(1024);
    private Future<Integer> futureRead = null;
    private List<Byte> bytesRead = new ArrayList<>();
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

    @Override
    public void readCall() {  // 被写线程调用
        synchronized (lockRead) {
            try {
                if (channel.isOpen() && futureRead != null && futureRead.isDone()) {
                    logger.info("收到数据");
                    if (futureRead.get() > 0) {
                        bufferRead.flip();
                        while (bufferRead.hasRemaining()) {
                            logger.info("读取一个byte");
                            bytesRead.add(bufferRead.get());
                        }
                        bufferRead.compact();
                        futureRead = channel.read(bufferRead);  // 主要问题在这里。
                    } else {
                        logger.info("读取结束");
                        futureRead = null;
                    }
                }
                lockRead.notify();
            } catch (Exception e) {
                logger.info("ChannelWrapper:异常{}-", e.getMessage(), e);
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
                byte[] bytesMsg = new byte[bytesRead.size()];
                for (int i = 0; i < bytesRead.size(); i++) {
                    bytesMsg[i] = bytesRead.get(i);
                }
                String msg = new String(bytesMsg, "UTF-8");
                logger.info("接受数据-{}", msg);
                bytesRead = new ArrayList<>();
                lockRead.notify();
                return msg;
            } catch (Exception e) {
                logger.info("ChannelWrapper:异常{}-", e.getMessage(), e);
            }

        }
        return null;
    }

    @Override
    public void writeCall() {

    }


    @Override
    public boolean isClosed() {
        return !channel.isOpen();
    }
}
