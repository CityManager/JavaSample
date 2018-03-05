package ind.xwm.imooc.concurrence.aio.future.wrapper;

import java.nio.channels.AsynchronousSocketChannel;

public class ChannelWrapper implements Wrapper {

    private AsynchronousSocketChannel channel;

    public ChannelWrapper(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public boolean isReadDone() {
        return false;
    }

    @Override
    public void readCall() {

    }

    @Override
    public void writeCall() {

    }

    @Override
    public String get() {
        return null;
    }

    @Override
    public void read() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
