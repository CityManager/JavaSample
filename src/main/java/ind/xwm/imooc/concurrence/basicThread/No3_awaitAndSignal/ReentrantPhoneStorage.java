package ind.xwm.imooc.concurrence.basicThread.No3_awaitAndSignal;

import ind.xwm.imooc.concurrence.basicThread.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * ReentrantLock 使用
 * 可以通过Lock对象获取多个 条件对象，
 * 线程获得锁后，条件不满足的情况下，可以在条件a下 await , 然后给特定条件b信号
 * await在特定条件b上的线程接受到信号后继续执行，后续执行结束，可以给刚才的a条件信号，唤醒刚才的线程，
 * 也可以给条件c发信号， 总之可以灵活处理
 * @param <T>
 */
public class ReentrantPhoneStorage<T> implements Storage<T> {
    private static final Lock lock = new ReentrantLock();
    private static final Condition full = lock.newCondition();
    private static final Condition empty = lock.newCondition();
    private static int MAX = 20;

    private int series;
    private int index = -1;
    private List<T> storageList = new ArrayList<T>(MAX);

    public int getSeries() {
        lock.lock();
        try {
            return series;
        } finally {
            lock.unlock();
        }

    }

    public void storage(T t) throws Exception {
        long threadID = Thread.currentThread().getId();
        lock.lock();
        try {
            while (index >= MAX) {
                System.out.println("线程" + threadID + ":缓存区满，无法存放产品");
                full.await();
            }
            storageList.add(t);
            series++;
            index++;
            empty.signal();
            System.out.println("线程" + threadID + ":成功将产品存放进缓存区");
        } finally {
            lock.unlock();
        }
    }

    public T takeOut() throws Exception {
        long threadID = Thread.currentThread().getId();
        lock.lock();
        try {
            while (index < 0) {
                System.out.println("线程" + threadID + ":缓存区空，无法获取产品");
                empty.await(); // 锁等待
            }
            T t = storageList.remove(index);
            index--;
            full.signal(); // 发信号通知其他所有等在锁的线程
            System.out.println("线程" + threadID + ":成功将产品从缓存区取出");
            return t;
        } finally {
            lock.unlock();
        }

    }
}
