package ind.xwm.imooc.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by XuWeiman on 2017/4/12.
 * 学习Log4j
 */
public class Log4jBar {
    private static final Logger logger = LogManager.getLogger(Log4jBar.class);
    public void foo() {
        logger.info("foo:" + this.getClass().getCanonicalName());
    }
}
