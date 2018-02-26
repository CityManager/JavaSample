package ind.xwm.imooc.concurrence.nio.reactor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class EchoServer implements Runnable {
    private static Logger logger = LogManager.getLogger(EchoServer.class);
    private Selector selector;

    public EchoServer() {
        try {
            selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(8000));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            logger.info("EchoServer:启动服务失败:{}-", e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        EchoEvenLoop loop = new EchoEvenLoop(selector);
        new Thread(loop).start();
    }
}
