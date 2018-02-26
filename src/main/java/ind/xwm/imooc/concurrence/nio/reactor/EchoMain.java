package ind.xwm.imooc.concurrence.nio.reactor;

public class EchoMain {

    public static void main(String[] args) {
        new Thread(new EchoServer()).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        new Thread(new EchoClient()).start();
    }
}
