package ind.xwm.imooc.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by XuWeiman on 2017/9/30.
 * sqlite3
 */
public class SqliteJDBC {
    private static Logger logger = LogManager.getLogger(SqliteJDBC.class);

    public static void main(String args[]) {
        Connection c = null;
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = c.createStatement();
            String sql = "CREATE TABLE COMPANY " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " AGE            INT     NOT NULL, " +
                    " ADDRESS        CHAR(50), " +
                    " SALARY         REAL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            logger.info(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    logger.info("异常-", e);
                }
            }
        }
        logger.info("Opened database successfully");
    }
}
