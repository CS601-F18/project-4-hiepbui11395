package cs601.project4.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnectionUtils {
    public static Connection getConnection() throws SQLException{

//        String hostName = "sql.cs.usfca.edu";
//        String hostName = "127.0.0.1";
//        String dbName = "user24";
//        String userName = "user24";
//        String password = "user24";



        String hostName = "127.0.0.1";
        String dbName = "user24";
        String userName = "root";
        String password = "trieutran1705";

        return getConnection(hostName, dbName, userName, password);
    }

    /**
     *
     * @param hostName
     * @param schema
     * @param username
     * @param password
     * @return Connection to mysql Database
     * @throws SQLException
     */
    private static Connection getConnection(String hostName, String schema,
                                            String username, String password)
            throws SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Can not find driver!");
            e.printStackTrace();
        }
        String connectionUrl = "jdbc:mysql//" + hostName +  ":3306/" + schema;
        //Must set time zone explicitly in newer versions of mySQL.
//        String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Connection connection = DriverManager.getConnection(connectionUrl,
                username, password);
        return connection;
    }
}
