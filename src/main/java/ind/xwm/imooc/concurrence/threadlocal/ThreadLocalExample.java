package ind.xwm.imooc.concurrence.threadlocal;

import org.apache.commons.lang3.RandomUtils;

/**
 * Created by XuWeiman on 2017/7/28.
 * ThreadLocal 共享实例
 */
public class ThreadLocalExample {
    private static class SharableBean {
        private static final ThreadLocal local = new ThreadLocal<String>() {
            @Override
            protected String initialValue() {
                return "Hello World";
            }
        };

        public String getSharedProperty() {
            return (String) local.get();
        }

        public void setSharedProperty(String sharedProperty) {
            local.set(sharedProperty);
        }
    }


    private static class ClientThread extends Thread {
        private SharableBean sharableBean;

        public ClientThread(SharableBean sharableBean) {
            this.sharableBean = sharableBean;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    this.sharableBean.setSharedProperty(this.sharableBean.getSharedProperty() + String.valueOf(i));
                    System.out.println("Thread[" + Thread.currentThread().getName() + "]" +
                            "--sharedProperty[" + this.sharableBean.getSharedProperty() + "]");
                    sleep(RandomUtils.nextInt(20, 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        SharableBean sharableBean = new SharableBean();
        ClientThread thread1 = new ClientThread(sharableBean);
        ClientThread thread2 = new ClientThread(sharableBean);
        ClientThread thread3 = new ClientThread(sharableBean);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}



