package ind.xwm.imooc.concurrence.basicThread.No2_Reentrancy;

import java.util.ArrayList;
import java.util.List;

/**
 * 同一线程内 同一个锁具有可重入性
 * 就是说 一个线程内多次进入同一个加锁对象的同步方法或者同步块（不一定是同一个方法或者同步块），
 * 只有在第一次获取到锁的那个同步方法或者同步块里面释放锁，才算真正意义上的锁释放
 */
public class Reentrancy implements Runnable {
    private ReentrancyChild child;

    public Reentrancy(ReentrancyChild child) {
        this.child = child;
    }

    @Override
    public void run() {
        child.doChildThing();
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrancyChild child = new ReentrancyChild();
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<10; i++) {
            threads.add(new Thread(new Reentrancy(child)));
        }

        for(Thread t: threads) {
            t.start();
        }

        for(Thread t: threads) {
            t.join();
        }
    }
}


class ReentrancyChild extends ReentrancyParent {
    public synchronized void doChildThing() {
        long id = Thread.currentThread().getId();
        System.out.println("线程" + id + ":正在执行子类方法doChildThing");
        doSomething();
    }

    public synchronized void doSomething() {
        long id = Thread.currentThread().getId();
        System.out.println("线程" + id + ":正在执行子类方法doSomething");
        doParentThing();
    }
}

class ReentrancyParent {
    public synchronized void doParentThing() {
        long id = Thread.currentThread().getId();
        System.out.println("线程" + id + ":正在执行父类方法doParentThing");
    }
}
