package ind.xwm.imooc.concurrence.aio.future;

import ind.xwm.imooc.concurrence.aio.future.container.AIOWrapperContainer;
import ind.xwm.imooc.concurrence.aio.future.handler.ChannelHandler;
import ind.xwm.imooc.concurrence.aio.future.worker.ReadWorker;
import ind.xwm.imooc.concurrence.aio.future.worker.WriteWorker;
import ind.xwm.imooc.concurrence.aio.future.wrapper.ChannelWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 广播传播器服务端
 * 设计了几个角色：
 * 1. AIOFutureServer ： 服务端启动器，同时也是 accept loop，用于接受channel并包装为wrapper
 * 2. ChannelWrapper ： channel包装器，对外开发读取接口
 * 3. AIOWrapperContainer： wrapper 的存放容器，接受accept后包装的wrapper，并在内部持有一个清理容器元素线程
 * 4. ReadWorker 和 WriteWorker ： 异步读/写循环器，通过容器获取wrapper，并调用wrapper提供的读写接口
 * 5. ChannelHandler ： 业务逻辑处理器（其实可以用于具体的业务分发），获取channel数据，并进行业务处理
 */
public class AIOFutureServer {
    private static Logger logger = LogManager.getLogger(AIOFutureServer.class);
    private static int PORT = 7878;
    private static String IP = "127.0.0.1";
    // private List<Future<AsynchronousSocketChannel>> acceptFutureList = new ArrayList<>();
    private AIOWrapperContainer container = new AIOWrapperContainer();
    private Executor executor = Executors.newCachedThreadPool();

    public void startServer() {
        try {
            AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
            if (serverChannel.isOpen()) {
                serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, false);
                serverChannel.bind(new InetSocketAddress(IP, PORT));
                logger.info("AIOFutureServer: waiting for connections...");
                // 先启动 成员信息传播器
                ChannelHandler handler = new ChannelHandler(container);
                ReadWorker readWorker = new ReadWorker(container);
                WriteWorker writeWoker = new WriteWorker(container);
                executor.execute(handler);
                executor.execute(readWorker);
                executor.execute(writeWoker);
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
                    logger.info("获得一个连接");
                    container.add(new ChannelWrapper(channel));
                }
            } else {
                logger.info("AIOFutureServer:ServerSocketChannel open failed.");
            }
        } catch (Exception e) {
            logger.info("AIOFutureServer:异常{}-", e.getMessage(), e);
        }
    }
}
