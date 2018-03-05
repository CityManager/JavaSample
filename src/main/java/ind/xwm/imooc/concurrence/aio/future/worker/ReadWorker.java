package ind.xwm.imooc.concurrence.aio.future.worker;

import ind.xwm.imooc.concurrence.aio.future.wrapper.Wrapper;

import java.util.ArrayList;
import java.util.List;

public class ReadWorker implements Runnable {

    private List<Wrapper> wrappers = new ArrayList<>();

    public void setWrappers(List<Wrapper> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public void run() {
        while(true) {
            for(Wrapper wrapper: wrappers) {
                wrapper.readCall();
            }
        }
    }
}
