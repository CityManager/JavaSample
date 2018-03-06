package ind.xwm.imooc.concurrence.aio.future;

public class AIOFutureClientMain {
    public static void main(String[] args) {
        for(int i=0; i<3; i++) {
            AIOFutureClient clientOnFuture = new AIOFutureClient();
            new Thread(clientOnFuture).start();
        }
    }
}
