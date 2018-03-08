package ind.xwm.imooc.concurrence.aio.future.worker;

import ind.xwm.imooc.concurrence.aio.future.container.AIOWrapperContainer;
import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 循环读取器，遍历容器中的wrapper，并调用wrapper的readCall接口
 */
public class ReadWorker implements Runnable {
    private static Logger logger = LogManager.getLogger(ReadWorker.class);

    private AIOWrapperContainer container;

    public ReadWorker(AIOWrapperContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        logger.info("循环读取线程启动.");
        while (true) {
            AIOWrapper wrapper = container.pull();
            if (wrapper != null) {
                wrapper.readCall();
            }
            container.push(wrapper);  // 固定搭配操作
        }
    }
}
