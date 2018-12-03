package cs601.project4.jdbc;

import cs601.project4.utils.Config;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlConnectionUtils {
    private static BasicDataSource dataSource;

    public static synchronized BasicDataSource getDataSource() {
        if (dataSource == null) {

            Properties config = Config.getInstance();
            String hostName = config.getProperty("hostname");
            int port = Integer.parseInt(config.getProperty("port"));
            String dbName = config.getProperty("dbname");
            String userName = config.getProperty("dbusername");
            String password = config.getProperty("dbpassword");
            String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            dataSource = new BasicDataSource();
            System.out.println("\t Database connection: " + "jdbc:mysql://" + hostName + ":" + port + "/" + dbName);
            dataSource.setUrl("jdbc:mysql://" + hostName + ":" + port + "/" + dbName + timeZoneSettings);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);

            dataSource.setMinIdle(5);
            dataSource.setMaxIdle(10);
            dataSource.setMaxOpenPreparedStatements(50);
        }
        return dataSource;
    }

    /**
     * Init the variable for My SQL Connection
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {

        Properties config = Config.getInstance();
        String hostName = config.getProperty("hostname");
        int port = Integer.parseInt(config.getProperty("port"));
        String dbName = config.getProperty("dbname");
        String userName = config.getProperty("dbusername");
        String password = config.getProperty("dbpassword");

        return getConnection(hostName, port, dbName, userName, password);
    }

    /**
     * @param hostName
     * @param schema
     * @param username
     * @param password
     * @return ConnectionUtil to mysql Database
     * @throws SQLException
     */
    private static Connection getConnection(String hostName, int port, String schema,
                                            String username, String password)
            throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Can not find driver!");
            e.printStackTrace();
        }
        String connectionUrl = "jdbc:mysql://" + hostName + ":" + port + "/" + schema;
        //Must set time zone explicitly in newer versions of mySQL.
        String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Connection connection = DriverManager.getConnection(connectionUrl + timeZoneSettings,
                username, password);
        return connection;
    }
}
