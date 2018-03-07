package ind.xwm.imooc.concurrence.aio.future.container;

import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AIOWrapperContainer {
    private static Logger logger = LogManager.getLogger(AIOWrapperContainer.class);
    private ConcurrentLinkedQueue<AIOWrapper> aioWrappers = new ConcurrentLinkedQueue<>();

    private Set<AIOWrapper> aioWrapperSet = new HashSet<>();
    private final Object lock = new Object();

    public AIOWrapperContainer() {
        super();
        Executor executor = Executors.newFixedThreadPool(1);
        executor.execute(() -> {
            logger.info("AIOWrapperContainer:容器清理线程启动");
            while(true) {
                try {
                    this.iteratorRemove();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.info("ChannelHandler:异常{}-", e.getMessage(), e);
                }
            }
        });
    }

    public void push(AIOWrapper aioWrapper) {
        if(aioWrapper != null && !aioWrapper.isClosed()) {
            aioWrappers.add(aioWrapper);
        }

        /*  // 避免限制到 ReadWorker 和 WriteWorker 对异步的操作
        if (aioWrapper == null) {
            return;
        }
        synchronized (lock) {
            if (aioWrapper.isClosed()) {
                logger.info("channel已经断开，不放回队列");
                if (aioWrapperSet.contains(aioWrapper)) {
                    aioWrapperSet.remove(aioWrapper);
                }
            } else {
                aioWrappers.add(aioWrapper);
                if (!aioWrapperSet.contains(aioWrapper)) {
                    aioWrapperSet.add(aioWrapper);
                }
            }
        }
        */

    }

    public AIOWrapper pull() {
        return aioWrappers.poll();
    }


    /**
     * 这里使用同步，会限制到 ReadWorker 和 WriteWorker 对异步的操作
     * 比较好的做法是 push 方法的去除对容器的删除操作代码。
     * 另起线程 对 容器 进行清理，让该线程与 本方法进行竞争
     *
     * 见 本类的构造方法和 iteratorRemove 方法
     *
     * @param caller 业务执行器
     */
    public void iteratorCall(BaseContainerCaller caller) {
        synchronized (lock) {
            logger.info("===========");
            for (AIOWrapper aioWrapper : aioWrapperSet) {
                logger.info("aioWrapper");
                caller.setAIOWrapper(aioWrapper);
                caller.call();
            }
            logger.info("===========");
        }

    }

    /**
     * 清理容器中 不可用的 AIOWrapper
     */
    private void iteratorRemove() {
        synchronized (lock) {
            aioWrapperSet.removeIf(AIOWrapper::isClosed);
        }
    }

    public void add(AIOWrapper aioWrapper) {
        if(aioWrapper != null && !aioWrapper.isClosed()) {
            synchronized (lock) {
                aioWrapperSet.add(aioWrapper);
                aioWrappers.add(aioWrapper);
            }
        }
    }

}
