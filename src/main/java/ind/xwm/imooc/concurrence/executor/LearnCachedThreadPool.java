package ind.xwm.imooc.concurrence.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LearnCachedThreadPool {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0; i<10;i++) {
            int finalI = i;
            //Thread.sleep(200);
            executorService.submit(()-> System.out.println("线程名：" + Thread.currentThread().getName() + " -- " + finalI));
        }
        executorService.shutdown();
    }
}
