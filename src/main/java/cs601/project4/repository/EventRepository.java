package cs601.project4.repository;

import cs601.project4.entity.Event;
import cs601.project4.jdbc.ConnectionUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    private static EventRepository instance;

    public static synchronized EventRepository getInstace() {
        if (instance == null) {
            instance = new EventRepository();
        }
        return instance;
    }

    private final String SQL_INSERT = "insert into `event` (`userId`,`name`,`numTickets`,`numTicketsAvail`) " +
            "values (?,?,?,?)";

    private final String SQL_UPDATE = "update `event` set `userId`=?,`name`=?,`numTickets`=?,`numTicketsAvail`=? " +
            "where `id`=?";

    public List<Event> gets() {
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from `event`")) {
            List<Event> eventList = new ArrayList<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                eventList.add(new Event(rs));
            }
            return eventList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Event create(Event entity) {
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getUserId());
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getNumTickets());
            statement.setInt(4, entity.getNumTicketsAvail());

            int affectedRow = statement.executeUpdate();
            if (affectedRow == 0) {
                return null;
            }
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                    return entity;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update number of available ticket in db
     *
     * @param entity
     * @param numTickets
     * @throws SQLException
     */
    public void updateAvailableTicket(Event entity, int numTickets) {
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setLong(1, entity.getUserId());
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getNumTickets());
            statement.setInt(4, entity.getNumTicketsAvail() - numTickets);
            statement.setLong(5, entity.getId());
            statement.executeUpdate();
            entity.setNumTicketsAvail(entity.getNumTicketsAvail() - numTickets);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Event findById(long id) {
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from `event` where `id` = ?")) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Event event = new Event(rs);
                    return event;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
