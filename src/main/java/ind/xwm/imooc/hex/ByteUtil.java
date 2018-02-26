package ind.xwm.imooc.hex;

/**
 * Created by XuWeiman on 2017/7/21.
 * 字节工具
 */
public class ByteUtil {
    public static byte[] String2Bytes(String str) {
        return str.getBytes();
    }


    public static void main(String[] args) {
        byte[] bytes = String2Bytes("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        System.out.println(bytes.length);
        System.out.println(Integer.toBinaryString('0'));
        for(byte b: bytes) {
            System.out.println(b);
            System.out.println(Integer.toBinaryString(b));
        }
    }
}
