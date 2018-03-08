package ind.xwm.imooc.concurrence.aio.future.container;

import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;

import java.util.concurrent.Callable;

/**
 * wrapper容器遍历 wrapper处理业务的接口，便于handler等业务模块需要便利wrapper进行业务操作
 */
public abstract class BaseContainerCaller implements Callable {


    private AIOWrapper wrapper;

    public void setAIOWrapper(AIOWrapper aioWrapper) {
        this.wrapper = aioWrapper;
    }

    public AIOWrapper getWrapper() {
        return wrapper;
    }

    @Override
    public abstract Object call();
}
