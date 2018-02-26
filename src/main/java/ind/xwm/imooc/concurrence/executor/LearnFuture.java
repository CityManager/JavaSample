package ind.xwm.imooc.concurrence.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class LearnFuture {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Future<String>> futureList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i=0;i<10;i++) {
            int finalI = i;
            Future<String> future = executorService.submit(()->{
                System.out.println("线程名：" + Thread.currentThread().getName() + " -- " + finalI);
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return "Task " + finalI + " 完成";
            });
            futureList.add(future);  // 将结果缓存对象存入list
        }

        Thread.sleep(1000);
        for(Future<String> future: futureList) {
            System.out.println(future.get()); // future.get() 会阻塞当前线程，知道任务线程返回结果为止
        }


        List<FutureTask<String>> futureTasks = new ArrayList<>();
        for(int i=0;i<10;i++) {
            int finalI = i;
            FutureTask<String> futureTask= new FutureTask<String>(() -> {
                System.out.println("线程名：" + Thread.currentThread().getName() + " -- " + finalI);
                return "Task " + finalI + " 完成";
            });
            executorService.submit(futureTask);
            futureTasks.add(futureTask);
        }

        Thread.sleep(1000);
        for(FutureTask<String> futureTask: futureTasks) {
            System.out.println(futureTask.get());
        }

        executorService.shutdown();
    }
}
