package cs601.project4.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtil {

    public static BasicDataSource getMyConnection() {
        return MySqlConnectionUtils.getDataSource();
    }
}
