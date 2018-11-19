package cs601.project4.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtil {
    private static Connection connectionUtil;

    private ConnectionUtil(){}

    public static synchronized Connection getInstance() throws SQLException {
        if (connectionUtil == null) {
            connectionUtil = MySqlConnectionUtils.getConnection();
        }
        return connectionUtil;
    }
}
