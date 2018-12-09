package cs601.project4.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionUtil {

    /**
     * Get connection in MySQL Server
     * @return
     */
    public static BasicDataSource getMyConnection() {
        return MySqlConnectionUtils.getDataSource();
    }
}
