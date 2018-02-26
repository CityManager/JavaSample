package ind.xwm.imooc.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by XuWeiman on 2017/4/12.
 * 学习log4j的主类
 */
public class Log4jMain {
    private static final  Logger logger = LogManager.getLogger(Log4jMain.class);
    public static void main(String[] args) {
        logger.info("测试log4j");
        Log4jBar bar = new Log4jBar();
        bar.foo();
    }
}
