package ind.xwm.imooc.concurrence.basicThread;

public interface Storage<T> {
    int getSeries();
    void storage(T t) throws Exception;
    T takeOut() throws Exception;
}
