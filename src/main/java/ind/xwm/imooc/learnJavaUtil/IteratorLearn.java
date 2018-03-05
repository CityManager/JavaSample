package ind.xwm.imooc.learnJavaUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorLearn {
    public static void main(String[] args) {
        List<String> msgs = new ArrayList<>();
        msgs.add("测试1");
        msgs.add("测试2");
        msgs.add("测试3");
        msgs.add("测试4");
        msgs.add("测试5");
        msgs.add("测试6");

        Iterator<String> it = msgs.iterator();
        while(it.hasNext()) {
            String msg = it.next();
            System.out.println(msg);
            Iterator<String> it2 = msgs.iterator();
            while(it2.hasNext()) {
                String msg2 = it2.next();
                if(!msg.equals(msg2)) {
                    System.out.println("  --" + msg2);
                }
            }
            System.out.println("======================");
            it.remove();
        }
    }
}
