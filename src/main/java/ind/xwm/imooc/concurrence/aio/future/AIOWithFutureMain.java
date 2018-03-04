package ind.xwm.imooc.concurrence.aio.future;

public class AIOWithFutureMain {
    public static void main(String[] args) {
        AIOServerOnFuture serverOnFuture = new AIOServerOnFuture();
        serverOnFuture.startServer();
        for(int i=0; i<5; i++) {
            AIOClientOnFuture clientOnFuture = new AIOClientOnFuture();
            new Thread(clientOnFuture).start();
        }
    }
}
