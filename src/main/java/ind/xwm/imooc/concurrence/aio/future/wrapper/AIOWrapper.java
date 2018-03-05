package ind.xwm.imooc.concurrence.aio.future.wrapper;

public interface AIOWrapper {
    boolean isReadDone();
    void read();
    void readCall();
    String get();

    void writeCall();
    boolean isClosed();
}
