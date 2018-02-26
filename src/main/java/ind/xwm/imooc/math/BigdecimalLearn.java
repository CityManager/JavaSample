package ind.xwm.imooc.math;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigdecimalLearn {
    public static void main(String[] args) {
        BigDecimal amount = BigDecimal.valueOf(Long.valueOf("583772344"));
        BigDecimal b100 = new BigDecimal(100);
        MathContext mc = new MathContext(10);
        System.out.println(amount.divide(b100, mc).toPlainString());
        String a = "<a href=\"http://www.runoob.com/java/java-date-time.html\" rel=\"prev\" title=\"Java 日期时间\">Java 日期时间</a>";
        System.out.println(a.replaceAll("href=\"[\\s\\S]+\"", "href=\"#\""));
    }
}
