package cs601.project4.repository;

import cs601.project4.entity.Event;
import cs601.project4.jdbc.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRepository {
    private static EventRepository instance;
    private Connection connection;

    public static synchronized EventRepository getInstace() {
        if(instance == null){
            instance = new EventRepository();
            try {
                instance.connection = ConnectionUtil.getInstance();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
    private final String SQL_INSERT = "insert into `event` (`username`,`password`,`salt`,`email`,`phoneNumber`)" +
            "values (?,?,?,?,?)";

    public long save(Event entity) throws SQLException{
        return 0;
    }
}
