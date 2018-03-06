package ind.xwm.imooc.concurrence.aio.future.container;

import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AIOWrapperContainer {
    private static Logger logger = LogManager.getLogger(AIOWrapperContainer.class);
    private ConcurrentLinkedQueue<AIOWrapper> aioWrappers = new ConcurrentLinkedQueue<>();

    private Set<AIOWrapper> aioWrapperSet = new HashSet<>();
    private final Object lock = new Object();

    public void push(AIOWrapper aioWrapper) {
        if(aioWrapper == null) {
            return;
        }
        synchronized (lock) {
            if(aioWrapper.isClosed()) {
                logger.info("channel已经断开，不放回队列");
                if(aioWrapperSet.contains(aioWrapper)) {
                    aioWrapperSet.remove(aioWrapper);
                }
            } else {
                aioWrappers.add(aioWrapper);
                if(!aioWrapperSet.contains(aioWrapper)) {
                    aioWrapperSet.add(aioWrapper);
                }
            }
        }

    }

    public AIOWrapper pull() {
       return aioWrappers.poll();
    }


    public void iteratorCall(BaseContainerCaller caller) {
        synchronized (lock) {
            logger.info("===========");
            for (AIOWrapper aioWrapper : aioWrapperSet) {
                caller.setAIOWrapper(aioWrapper);
                caller.call();
            }
            logger.info("===========");
        }

    }

}
