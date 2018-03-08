package ind.xwm.imooc.concurrence.aio.future.worker;

import ind.xwm.imooc.concurrence.aio.future.container.AIOWrapperContainer;
import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 循环写出器，遍历容器中的wrapper，并调用wrapper的writeCall接口
 */
public class WriteWorker implements Runnable {
    private static Logger logger = LogManager.getLogger(WriteWorker.class);

    private AIOWrapperContainer container;

    public WriteWorker(AIOWrapperContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        logger.info("循环写出线程启动.");
        while (true) {
            AIOWrapper wrapper = container.pull();
            if (wrapper != null) {
                wrapper.writeCall();
            }
            container.push(wrapper);  // 固定搭配操作
        }
    }
}
