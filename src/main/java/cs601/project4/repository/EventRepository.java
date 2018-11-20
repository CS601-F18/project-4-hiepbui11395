package cs601.project4.repository;

import cs601.project4.entity.Event;
import cs601.project4.jdbc.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRepository {
    private static EventRepository instance;

    public static synchronized EventRepository getInstace() {
        if(instance == null){
            instance = new EventRepository();
        }
        return instance;
    }
    private final String SQL_INSERT = "insert into `event` (`name`,`description`,`location`,`date`,`active`)" +
            "values (?,?,?,?,?)";

    public long save(Event entity) throws SQLException{
        //TODO: Check event id to know it is add or update
        Connection connection = ConnectionUtil.getMyConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getDescription());
            statement.setString(3, entity.getLocation());
            statement.setObject(4, entity.getDate());
            statement.setBoolean(5, true);
            int affectedRow = statement.executeUpdate();
            if (affectedRow == 0) {
                throw new SQLException("Creating event failed - no row affected");
            }
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            } else {
                throw new SQLException("Creating event failed - no Id obtained");
            }
            return entity.getId();
        } finally {
            connection.close();
        }
    }
}
