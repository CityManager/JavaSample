package ind.xwm.imooc.concurrence.nio.reactor;

import ind.xwm.imooc.concurrence.nio.reactor.util.ReadWriteTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoClient implements Runnable {
    private static Logger logger = LogManager.getLogger(EchoClient.class);

    @Override
    public void run() {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            Selector selector = Selector.open();
            channel.register(selector,  SelectionKey.OP_CONNECT);
            channel.connect(new InetSocketAddress(8000));

            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.info("EchoClient异常{}-", e.getMessage(), e);
                }

                if (selector.select() > 0) {
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> it = keySet.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if (key.isReadable()) {
                            String msg = ReadWriteTool.readMsg(key);
                            logger.info("客户端{}:获取信息----{}", Thread.currentThread().getId(), msg);
                            channel.register(selector, SelectionKey.OP_WRITE);
                        } else if (key.isWritable()) {
                            ReadWriteTool.writeMsg(key, "哈哈，我是客户端数据");
                            logger.info("客户端{}:成功发送数据", Thread.currentThread().getId());
                            channel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isConnectable()) {  // 客户端一定要等待 finishConnect()
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            if (socketChannel.finishConnect()) {
                                channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            }
                        }
                        else {
                            logger.info("EchoClient:触发非目标事件为：{}", key.interestOps());
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.info("EchoClient异常{}-", e.getMessage(), e);
        }

    }


}
