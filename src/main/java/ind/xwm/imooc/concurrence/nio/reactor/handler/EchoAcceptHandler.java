package ind.xwm.imooc.concurrence.nio.reactor.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class EchoAcceptHandler implements Runnable {
    private static Logger logger = LogManager.getLogger(EchoAcceptHandler.class);

    private SelectionKey key;

    public EchoAcceptHandler(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        Selector selector = key.selector();
        try {
            SocketChannel channel = serverSocketChannel.accept();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
            logger.info("服务端成功接收客户端请求");
        } catch (IOException e) {
            logger.info("EchoAcceptHandler异常:{}-", e.getMessage(), e);
        }
    }
}
