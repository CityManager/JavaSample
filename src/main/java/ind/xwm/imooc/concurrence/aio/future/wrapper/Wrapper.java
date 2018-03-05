package ind.xwm.imooc.concurrence.aio.future.wrapper;

public interface Wrapper {
    boolean isReadDone();
    void readCall();
    void writeCall();
    String get();
    void read();
    boolean isClosed();
}
