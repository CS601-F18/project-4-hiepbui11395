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
    private final String SQL_INSERT = "insert into `event` (`userId`,`name`,`numTickets`)" +
            "values (?,?,?)";

    public long save(Event entity) throws SQLException{
        //TODO: Check event id to know it is add or update
        Connection connection = ConnectionUtil.getMyConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setLong(1, entity.getUserId());
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getNumTickets());
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

    public Event findById(long id) throws SQLException {
        Connection connection = ConnectionUtil.getMyConnection();
        try{
            PreparedStatement statement = connection.prepareStatement("select * from `event` where `id` = ?");
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Event event = new Event(rs);
                return event;
            } else{
                return null;
            }
        } finally {
            connection.close();
        }
    }
}
