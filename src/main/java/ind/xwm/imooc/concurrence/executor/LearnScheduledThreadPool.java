package ind.xwm.imooc.concurrence.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LearnScheduledThreadPool {
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(()-> System.out.println("Bee..."), 5, 3, TimeUnit.SECONDS);

        System.out.println(Integer.toBinaryString(CAPACITY));
        System.out.println(Integer.toBinaryString((-1 << COUNT_BITS) & CAPACITY));
        System.out.println(Integer.toBinaryString(-1 << COUNT_BITS));
        System.out.println(-1 << COUNT_BITS);
        System.out.println(Integer.toBinaryString(0 << COUNT_BITS));
    }
}
