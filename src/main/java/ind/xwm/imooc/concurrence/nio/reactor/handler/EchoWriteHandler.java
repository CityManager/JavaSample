package ind.xwm.imooc.concurrence.nio.reactor.handler;

import ind.xwm.imooc.concurrence.nio.reactor.util.ReadWriteTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class EchoWriteHandler implements Runnable {
    private static Logger logger = LogManager.getLogger(EchoWriteHandler.class);

    private SelectionKey key;

    public EchoWriteHandler(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        try {
            ReadWriteTool.writeMsg(key, "哈哈，我是服务端端数据");
            logger.info("EchoWriteHandler:服务端写出数据");
            // 写完一定要设置为 等待读
            Selector selector = key.selector();
            SocketChannel channel = (SocketChannel) key.channel();
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            logger.info("EchoWriteHandler异常:{}-", e.getMessage(), e);
        }
    }
}
