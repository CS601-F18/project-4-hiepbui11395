package cs601.project4.repository;

import cs601.project4.entity.Ticket;
import cs601.project4.jdbc.ConnectionUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository {
    private static TicketRepository instance;

    private TicketRepository(){}

    public static synchronized TicketRepository getInstance(){
        if(instance == null){
            instance = new TicketRepository();
        }
        return instance;
    }

    private final String SQL_INSERT = "insert into `ticket` (`userId`,`eventId`) " +
            "values (?,?)";

    private final String SQL_UPDATE = "update `ticket` set `userId`=?,`eventId`=? " +
            "where `id`=?";

    public Long create(long userId, long eventId) {
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                    PreparedStatement.RETURN_GENERATED_KEYS))
        {
            statement.setLong(1, userId);
            statement.setLong(2, eventId);

            int affectedRow = statement.executeUpdate();
            if (affectedRow == 0) {
                return null;
            }
            try(ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    return id;
                } else {
                    return null;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean transferTicket(List<Ticket> ticketList, long userId, long targetUserId, int numTickets){
        List<Ticket> updatedTicketList = new ArrayList<>();
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE))
        {
            for(int i=0; i<numTickets; i++){
                Ticket ticket = ticketList.get(i);
                statement.setLong(1, targetUserId);
                statement.setLong(2, ticket.getEventId());
                statement.setLong(3, ticket.getId());
                statement.executeUpdate();
                updatedTicketList.add(ticket);
            }
            return true;
        } catch (SQLException e) {
            //TODO: if have SQLException after I already update ticket but SQLException
            e.printStackTrace();
            return false;
        }
    }

    public List<Ticket> findTicketsByUserId(long userId) {
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from `ticket` where `userId` = ?"))
        {
            statement.setLong(1,userId);
            return getTicketsResultSet(statement);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Ticket> findTicketsByUserIdAndEventId(long userId, long eventId) {
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from `ticket` where `userId` = ? and `eventId`=?"))
        {
            statement.setLong(1,userId);
            statement.setLong(2, eventId);
            return getTicketsResultSet(statement);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private List<Ticket> getTicketsResultSet(PreparedStatement statement) throws SQLException {
        try(ResultSet rs = statement.executeQuery()) {
            List<Ticket> ticketList = new ArrayList<>();
            while (rs.next()) {
                Ticket ticket = new Ticket(rs);
                ticketList.add(ticket);
            }
            return ticketList;
        }
    }
}
