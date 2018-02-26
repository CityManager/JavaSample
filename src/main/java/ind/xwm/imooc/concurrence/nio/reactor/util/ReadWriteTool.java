package ind.xwm.imooc.concurrence.nio.reactor.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;

public class ReadWriteTool {
    private static Logger logger = LogManager.getLogger(ReadWriteTool.class);

    public static String readMsg(SelectionKey key) throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        SocketChannel channel = (SocketChannel) key.channel();
        StringBuilder msgBuilder = new StringBuilder();

        int readBytes = channel.read(readBuffer);
        while (readBytes > 0) {
            readBuffer.flip();
            decoder.decode(readBuffer, charBuffer, false);
            charBuffer.flip();
            while (charBuffer.hasRemaining()) {
                msgBuilder.append(charBuffer.get());
            }
            readBuffer.compact();
            charBuffer.clear();
            readBytes = channel.read(readBuffer);
        }

        return msgBuilder.toString();
    }


    public static void writeMsg(SelectionKey key, String msg) throws IOException {
        int readLength = 1024;
        ByteBuffer writeBuffer = ByteBuffer.allocate(readLength);
        SocketChannel channel = (SocketChannel) key.channel();

        byte[] msgBytes = msg.getBytes();
        int len = msgBytes.length;
        int subSectionSize = (len % readLength == 0) ? (len / readLength) : (len / readLength + 1);

        for (int i = 0; i <= subSectionSize - 1; i++) { // 分段读取msg

            int readStart = i * readLength;
            int readEnd = (i == subSectionSize - 1) ? len : (i + 1) * readLength;
            byte[] tempBytes = Arrays.copyOfRange(msgBytes, readStart, readEnd);
            writeBuffer.put(tempBytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
            writeBuffer.compact();
        }
    }
}
