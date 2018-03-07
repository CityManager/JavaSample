package ind.xwm.imooc.concurrence.aio.future.handler;

import ind.xwm.imooc.concurrence.aio.future.container.AIOWrapperContainer;
import ind.xwm.imooc.concurrence.aio.future.container.BaseContainerCaller;
import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChannelHandler implements Runnable {
    private static Logger logger = LogManager.getLogger(ChannelHandler.class);

    private AIOWrapperContainer container;

    public ChannelHandler(AIOWrapperContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        logger.info("handler 线程");
        while (true) {
            try {
                AIOWrapper wrapper = container.pull();
                if (wrapper != null) {
                    String msg = wrapper.readAsync(); // 异步读取没有结束时，获取到的数据就是null
                    if (StringUtils.isNotBlank(msg)) {
                        container.iteratorCall(new BaseContainerCaller() {
                            @Override
                            public Object call() {
                                AIOWrapper writeAioWrapper = getWrapper();
                                writeAioWrapper.write(msg);
                                return null;
                            }
                        });
                    }
                }
                container.push(wrapper);
            } catch (Exception e) {
                logger.info("ChannelHandler:异常{}-", e.getMessage(), e);
            }
        }

    }
}
