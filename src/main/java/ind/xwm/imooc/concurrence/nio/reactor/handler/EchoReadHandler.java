package ind.xwm.imooc.concurrence.nio.reactor.handler;

import ind.xwm.imooc.concurrence.nio.reactor.util.ReadWriteTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class EchoReadHandler implements Runnable {
    private static Logger logger = LogManager.getLogger(EchoReadHandler.class);

    private SelectionKey key;

    public EchoReadHandler(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        try {
            String msg = ReadWriteTool.readMsg(key);
            logger.info("服务端获取信息:{}", msg);
            // 读取完成一定要设置为 等待写
            Selector selector = key.selector();
            SocketChannel channel = (SocketChannel) key.channel();
            channel.register(selector, SelectionKey.OP_WRITE);
        } catch (IOException e) {
            logger.info("EchoReadHandler异常:{}-", e.getMessage(), e);
        }
    }


}
