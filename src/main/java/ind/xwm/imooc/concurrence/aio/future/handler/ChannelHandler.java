package ind.xwm.imooc.concurrence.aio.future.handler;

import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChannelHandler implements Runnable {
    private static Logger logger = LogManager.getLogger(ChannelHandler.class);

    private List<AIOWrapper> wrappers = new ArrayList<>();

    public void setWrappers(List<AIOWrapper> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (AIOWrapper wrapper : wrappers) {  // 没有remove操作,对同一个集合两次for
                    if (!wrapper.isClosed() && wrapper.isReadDone()) {
                        String msg = wrapper.get();
                        wrapper.read(); // 如果是这样
                        for (AIOWrapper wrapperForWrite : wrappers) {
                            if (!wrapperForWrite.equals(wrapper)) {
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
