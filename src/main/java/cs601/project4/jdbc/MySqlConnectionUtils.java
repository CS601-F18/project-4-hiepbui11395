package cs601.project4.jdbc;

import cs601.project4.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlConnectionUtils {
    /**
     * Init the variable for My SQL Connection
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{

        Properties config = Config.getInstance();
        String hostName = config.getProperty("hostname");
        int port = Integer.parseInt(config.getProperty("port"));
        String dbName = config.getProperty("dbname");
        String userName = config.getProperty("dbusername");
        String password = config.getProperty("dbpassword");

        return getConnection(hostName, port, dbName, userName, password);
    }

    /**
     *
     * @param hostName
     * @param schema
     * @param username
     * @param password
     * @return ConnectionUtil to mysql Database
     * @throws SQLException
     */
    private static Connection getConnection(String hostName, int port, String schema,
                                            String username, String password)
            throws SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Can not find driver!");
            e.printStackTrace();
        }
        String connectionUrl = "jdbc:mysql://" + hostName +  ":" + port + "/" + schema;
        //Must set time zone explicitly in newer versions of mySQL.
        String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Connection connection = DriverManager.getConnection(connectionUrl + timeZoneSettings,
                username, password);
        return connection;
    }
}
