package ind.xwm.imooc.concurrence.aio.future;

import ind.xwm.imooc.concurrence.aio.future.handler.ChannelHandler;
import ind.xwm.imooc.concurrence.aio.future.worker.ReadWorker;
import ind.xwm.imooc.concurrence.aio.future.worker.WriteWoker;
import ind.xwm.imooc.concurrence.aio.future.wrapper.ChannelWrapper;
import ind.xwm.imooc.concurrence.aio.future.wrapper.AIOWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 广播传播器服务端
 */
public class AIOFutureServer {
    private static Logger logger = LogManager.getLogger(AIOFutureServer.class);
    private static int PORT = 7878;
    private static String IP = "127.0.0.1";
    // private List<Future<AsynchronousSocketChannel>> acceptFutureList = new ArrayList<>();
    private List<AIOWrapper> wrappers = new ArrayList<>();
    private Executor executor = Executors.newFixedThreadPool(3);

    public void startServer() {
        try {
            AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
            if (serverChannel.isOpen()) {
                serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, false);
                serverChannel.bind(new InetSocketAddress(IP, PORT));
                logger.info("AIOFutureServer: waiting for connections...");
                // 先启动 成员信息传播器
                ChannelHandler handler = new ChannelHandler();
                handler.setWrappers(wrappers);
                ReadWorker readWorker = new ReadWorker();
                readWorker.setWrappers(wrappers);
                WriteWoker writeWoker = new WriteWoker();
                writeWoker.setWrappers(wrappers);
                executor.execute(handler);
                executor.execute(readWorker);
                executor.execute(writeWoker);
                // todo 增加一个 writeWorker
                // 开始接受请求
                while (true) { // accept 操作 一定要阻塞到结果返回,如果是异步写法,第二次调用,则容易出现null异常

//                    Future<AsynchronousSocketChannel> acceptFuture = serverChannel.accept();
//                    acceptFutureList.add(acceptFuture);
//                    for (Future<AsynchronousSocketChannel> f : acceptFutureList) {
//                        if (f.isDone()) {
//                            AsynchronousSocketChannel channel = f.get();
//                            logger.info("AIOFutureServer: 接受连接-{}", channel.getRemoteAddress());
//                            members.add(channel);
//                        }
//                    }
                    AsynchronousSocketChannel channel = serverChannel.accept().get();
                    wrappers.add(new ChannelWrapper(channel));
                    // 由 接受 looper 来控制请求的清理,只是移除, 因为确定是close,其他现场已经不会对其进行操作
                    wrappers.removeIf(AIOWrapper::isClosed);
                }
            } else {
                logger.info("AIOFutureServer:ServerSocketChannel open failed.");
            }
        } catch (Exception e) {
            logger.info("AIOFutureServer:异常{}-", e.getMessage(), e);
        }
    }
}
