package ind.xwm.imooc.concurrence.aio.future.wrapper;

public interface AIOWrapper {
    boolean isReadDone();
    void read();
    void readCall();
    String get();

    void write(String msg);
    void writeCall();
    boolean isClosed();
}
