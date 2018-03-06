package ind.xwm.imooc.concurrence.aio.future.worker;

import ind.xwm.imooc.concurrence.aio.future.container.AIOWrapperContainer;

public class WriteWoker implements Runnable {

    private AIOWrapperContainer container;

    public WriteWoker(AIOWrapperContainer container) {
        this.container = container;
    }

    @Override
    public void run() {

    }
}
