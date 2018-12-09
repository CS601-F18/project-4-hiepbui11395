package cs601.project4.jdbc;

import cs601.project4.utils.Config;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlConnectionUtils {
    private static BasicDataSource dataSource;

    public static synchronized BasicDataSource getDataSource() {
        if (dataSource == null) {
            Logger logger = LogManager.getLogger();
            Properties config = Config.getInstance();
            String hostName = config.getProperty("hostname");
            int port = Integer.parseInt(config.getProperty("port"));
            String dbName = config.getProperty("dbname");
            String userName = config.getProperty("dbusername");
            String password = config.getProperty("dbpassword");
            String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            dataSource = new BasicDataSource();
            logger.info("\t Database connection: " + "jdbc:mysql://" + hostName + ":" + port + "/" + dbName);
            dataSource.setUrl("jdbc:mysql://" + hostName + ":" + port + "/" + dbName + timeZoneSettings);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);

            dataSource.setMinIdle(5);
            dataSource.setMaxIdle(10);
            dataSource.setMaxOpenPreparedStatements(50);
        }
        return dataSource;
    }
}
