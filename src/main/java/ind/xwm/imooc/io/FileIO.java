package ind.xwm.imooc.io;

import java.io.*;

public class FileIO {
    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\XuWeiman\\Desktop\\18320350238.txt";
        File file = new File(fileName);
        InputStream in = new FileInputStream(file);
        byte[] buff = new byte[1024];
        if(in.read(buff) != -1) {
            System.out.println(buff);
        }
        in.close();
        if(file != null) {
            System.out.println(file.getAbsolutePath());
        }
    }
}
