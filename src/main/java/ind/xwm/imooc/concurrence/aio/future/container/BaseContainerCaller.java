package ind.xwm.imooc.concurrence.aio.future.container;

import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;

import java.util.concurrent.Callable;

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
