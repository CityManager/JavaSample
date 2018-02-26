package ind.xwm.imooc.concurrence.basicThread.No1_WaitAndNotify;

import ind.xwm.imooc.concurrence.basicThread.Storage;

import java.util.ArrayList;
import java.util.List;

/**
 * phone 产品缓存区
 * 同步方法 是通过实例自己进行加锁和释放锁
 * 同步代码块 则需要额外的对象来充当锁
 */
public class PhoneStorage<T> implements Storage<T> {
    private static int MAX = 20;
    private final static String LOCK = "LOCK";

    private int series;
    private int index = -1;


    private List<T> storageList = new ArrayList<T>(MAX);

    public int getSeries() {
        synchronized (LOCK) {
            return series;
        }
    }

    public void storage(T t) throws Exception {
        long threadID = Thread.currentThread().getId();
        synchronized (LOCK) {
            while (index >= MAX) {
                System.out.println("线程" + threadID + ":缓存区满，无法存放产品");
                LOCK.wait();
            }
            storageList.add(t);
            series++;
            index++;
            LOCK.notifyAll();  // 是否会 自动 notify ？
        }
        System.out.println("线程" + threadID + ":成功将产品存放进缓存区");
    }


    public synchronized T takeOut() throws Exception {
        long threadID = Thread.currentThread().getId();
        synchronized (LOCK) {
            while (index < 0) {
                System.out.println("线程" + threadID + ":缓存区空，无法获取产品");
                LOCK.wait();
            }
            T t = storageList.remove(index);
            index--;
            LOCK.notifyAll();
            System.out.println("线程" + threadID + ":成功将产品从缓存区取出");
            return t;
        }
    }
}
