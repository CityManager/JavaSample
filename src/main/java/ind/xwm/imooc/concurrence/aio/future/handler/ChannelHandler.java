package ind.xwm.imooc.concurrence.aio.future.handler;

import ind.xwm.imooc.concurrence.aio.future.wrapper.Wrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChannelHandler implements Runnable {
    private static Logger logger = LogManager.getLogger(ChannelHandler.class);

    private List<Wrapper> wrappers = new ArrayList<>();

    public void setWrappers(List<Wrapper> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public void run() {
        while (true) {
            try {

                for (Wrapper wrapper : wrappers) { // todo 待调整为 wrapper 的方式
                    if (wrapper.isReadDone()) {
                        String msg = wrapper.get();
                        wrapper.read(); // 如果是这样
                        for (Wrapper wrapper4Write : wrappers) {
                            if (!wrapper4Write.equals(wrapper)) {
                                // todo 写入内容
                            }
                        }
                    }
                }

            } catch (Exception e) {
                logger.info("ChannelHandler:异常{}-", e.getMessage(), e);
            }
        }

    }
}
