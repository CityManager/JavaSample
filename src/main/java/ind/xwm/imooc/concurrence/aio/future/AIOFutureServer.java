package ind.xwm.imooc.concurrence.aio.future;

import ind.xwm.imooc.concurrence.aio.future.handler.ChannelHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 广播传播器服务端
 */
public class AIOFutureServer {
    private static Logger logger = LogManager.getLogger(AIOFutureServer.class);
    private static int PORT = 7878;
    private static String IP = "127.0.0.1";
    private List<Future<AsynchronousSocketChannel>> acceptFutureList = new ArrayList<>();
    private List<AsynchronousSocketChannel> members = new ArrayList<>();

    public void startServer() {
        try {
            AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
            if (serverChannel.isOpen()) {
                serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, false);
                serverChannel.bind(new InetSocketAddress(IP, PORT));
                logger.info("AIOFutureServer: waiting for connections...");
                // 先启动 成员信息传播器
                new Thread(new ChannelHandler(members)).start();
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
                    members.add(channel);
                }
            } else {
                logger.info("AIOFutureServer:ServerSocketChannel open failed.");
            }
        } catch (Exception e) {
            logger.info("AIOFutureServer:异常{}-", e.getMessage(), e);
        }
    }
}