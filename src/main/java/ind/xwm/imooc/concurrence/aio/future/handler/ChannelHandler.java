package ind.xwm.imooc.concurrence.aio.future.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class ChannelHandler implements Runnable {
    private static Logger logger = LogManager.getLogger(ChannelHandler.class);

    private List<AsynchronousSocketChannel> members;

    private CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
    private Map<Future<Integer>, AsynchronousSocketChannel> inFutureMap = new HashMap<>();
    private Map<Future<Integer>, ByteBuffer> readBufferMap = new HashMap<>();


    public ChannelHandler(List<AsynchronousSocketChannel> members) {
        this.members = members;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (AsynchronousSocketChannel channel : members) {
                    ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                    Future<Integer> readFuture = channel.read(buffer);
                    readBufferMap.put(readFuture, buffer);
                    inFutureMap.put(readFuture, channel);
                }

                Iterator<Map.Entry<Future<Integer>, ByteBuffer>> it = readBufferMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Future<Integer>, ByteBuffer> entry = it.next();
                    Future<Integer> future = entry.getKey();
                    if (future.isDone()) {  // 读取完成
                        if (future.get() != -1) {
                            // 读取
                            ByteBuffer buffer = entry.getValue();
                            buffer.flip();
                            CharBuffer charBuffer = decoder.decode(buffer);
                            charBuffer.flip();
                            String msg = charBuffer.toString();
                            if("close".equals(msg.trim())) {
                                // 为什么要在服务端这边断开，在客户端直接断开不就可以了。。
                                AsynchronousSocketChannel channel = inFutureMap.get(future);
                                members.remove(channel);
                                channel.close();
                            } else {
                                // 写出
                                for(AsynchronousSocketChannel channel : members) {
                                    channel.write(ByteBuffer.wrap(msg.getBytes()));
                                }
                            }
                        }
                        it.remove();
                    }
                }

            } catch (Exception e) {
                logger.info("ChannelHandler:异常{}-", e.getMessage(), e);
            }
        }

    }
}
