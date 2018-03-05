package ind.xwm.imooc.learnJavaUtil.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorThread {
    public static void main(String[] args) {
        final List<String> msgs = new ArrayList<>();
        msgs.add("测试1");
        msgs.add("测试2");
        msgs.add("测试3");
        msgs.add("测试4");
        msgs.add("测试5");
        msgs.add("测试6");

        for(int i=0;i<2;i++) { // 不行, iterator 也不能在多线程中remove
            new Thread(() -> {
                long threadId = Thread.currentThread().getId();
                Iterator<String> it = msgs.iterator();
                while (it.hasNext()) {
                    String msg = it.next();
                    System.out.println("remove " + threadId + ":" + msg);
                    //it.remove();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
