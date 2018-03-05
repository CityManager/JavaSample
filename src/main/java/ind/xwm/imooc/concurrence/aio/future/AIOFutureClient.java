package ind.xwm.imooc.concurrence.aio.future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class AIOFutureClient implements Runnable {
    private static Logger logger = LogManager.getLogger(AIOFutureClient.class);

    private static int PORT = 7878;
    private static String IP = "127.0.0.1";
    private CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

    private static String[] msgs = new String[]{
            "你好", "我是客户端", "我们做朋友吧", "Good Bye", "close"
    };

    @Override
    public void run() {
        try {
            AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
            if (channel.isOpen()) {
                channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
                channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                Void aVoid = channel.connect(new InetSocketAddress(IP, PORT)).get();
                logger.info("成功连接到服务器-{}", aVoid);
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                if (aVoid == null) {
                    long id = Thread.currentThread().getId();
                    for (String msg : msgs) {
                        logger.info("{}-client-send:{}", id, msg);
                        byteBuffer.put(msg.getBytes());
                        byteBuffer.flip();
                        channel.write(byteBuffer).get();
                        byteBuffer.clear();

                        if (channel.read(byteBuffer).get() != -1) {
                            byteBuffer.flip();
                            CharBuffer charBuffer = decoder.decode(byteBuffer);
                            logger.info("{}-client-received:{}", id, charBuffer.toString());
                        }
                    }

                }

            } else {
                logger.info("AIOFutureClient:SocketChannel open failed.");
            }
        } catch (Exception e) {
            logger.info("AIOFutureClient:异常{}-", e.getMessage(), e);
        }
    }
}
