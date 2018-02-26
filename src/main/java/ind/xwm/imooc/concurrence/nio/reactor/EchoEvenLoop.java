package ind.xwm.imooc.concurrence.nio.reactor;

import ind.xwm.imooc.concurrence.nio.reactor.handler.EchoAcceptHandler;
import ind.xwm.imooc.concurrence.nio.reactor.handler.EchoReadHandler;
import ind.xwm.imooc.concurrence.nio.reactor.handler.EchoWriteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoEvenLoop implements Runnable {
    private static Logger logger = LogManager.getLogger(EchoServer.class);

    private Selector selector;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public EchoEvenLoop(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (selector.select() > 0) {
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> it = keySet.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();  // 一定要先去除
                        if (key.isAcceptable()) {  // 判断各个触发的事件，将任务丢给线程池处理？
                            executorService.execute(new EchoAcceptHandler(key));
                        } else if (key.isReadable()) {
                            executorService.execute(new EchoReadHandler(key));
                        } else if (key.isWritable()) {
                            executorService.execute(new EchoWriteHandler(key));
                        } else {
                            logger.info("EchoEvenLoop:触发非目标事件为：{}", key.interestOps());
                        }
                    }
                }
            } catch (IOException e) {
                logger.info("EchoEvenLoop:轮询异常{}-", e.getMessage(), e);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.info("EchoEvenLoop:异常{}-", e.getMessage(), e);
            }
        }
    }
}
