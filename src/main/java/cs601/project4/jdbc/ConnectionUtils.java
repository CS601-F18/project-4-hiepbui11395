package cs601.project4.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {
    public static Connection getConnection() throws SQLException {
        return MySqlConnectionUtils.getConnection();
    }
}
