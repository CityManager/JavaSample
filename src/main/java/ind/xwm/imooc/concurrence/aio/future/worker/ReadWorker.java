package ind.xwm.imooc.concurrence.aio.future.worker;

import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ReadWorker implements Runnable {
    private static Logger logger = LogManager.getLogger(ReadWorker.class);

    private List<AIOWrapper> wrappers = new ArrayList<>();

    public void setWrappers(List<AIOWrapper> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public void run() {
        logger.info("循环读取线程启动.");
        while(true) {
            for(AIOWrapper wrapper: wrappers) {
                wrapper.readCall();
            }
        }
    }
}
