package ind.xwm.imooc.concurrence.aio.future.worker;

import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;

import java.util.ArrayList;
import java.util.List;

public class ReadWorker implements Runnable {

    private List<AIOWrapper> wrappers = new ArrayList<>();

    public void setWrappers(List<AIOWrapper> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public void run() {
        while(true) {
            for(AIOWrapper wrapper: wrappers) {
                wrapper.readCall();
            }
        }
    }
}
