package ind.xwm.imooc.concurrence.nio.basic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class NIOStart {
    public static void main(String[] args) throws IOException {
        // 处理中文问题
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer charBuffer = CharBuffer.allocate(1024);

        RandomAccessFile file = new RandomAccessFile("C:\\Users\\XuWeiman\\Desktop\\NIOStart.txt", "rw");
        FileChannel fileChannel = file.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int bytesRead = fileChannel.read(byteBuffer);
        while (bytesRead != -1) {
            System.out.println("本次读取字节数" + bytesRead);
            byteBuffer.flip();
            decoder.decode(byteBuffer, charBuffer, false); // 处理中文问题
            charBuffer.flip();
            while (charBuffer.hasRemaining()) {
                System.out.print(charBuffer.get());
            }
            byteBuffer.compact();  // Buffer 内部读取位置指向调整(与clear方法有不同点)
            charBuffer.clear();
            bytesRead = fileChannel.read(byteBuffer);
        }

        String text = "测试NIO写文件，不使用Selector";
        byte[] bytes = text.getBytes();
        byteBuffer =  ByteBuffer.allocate(bytes.length);  // 空间太小的话，会导致存放失败
        byteBuffer.put(bytes);
        byteBuffer.flip();  // 写入也是要flip
        fileChannel.write(byteBuffer);
    }
}
