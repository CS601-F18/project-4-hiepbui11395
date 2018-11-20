package cs601.project4.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtil {

    public static Connection getMyConnection() throws SQLException {
        return MySqlConnectionUtils.getConnection();
    }
}
