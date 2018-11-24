package cs601.project4.repository;

import cs601.project4.entity.Ticket;
import cs601.project4.jdbc.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository {
    private static TicketRepository instance;

    private TicketRepository(){};

    public static synchronized TicketRepository getInstance(){
        if(instance == null){
            instance = new TicketRepository();
        }
        return instance;
    }

    public List<Ticket> findTicketsByUserId(long userId) throws SQLException {
        Connection connection = ConnectionUtil.getMyConnection();
        try{
            PreparedStatement statement = connection.prepareStatement(
                    "select * from `ticket` where `userId` = ?");
            statement.setLong(1,userId);
            ResultSet rs = statement.executeQuery();
            List<Ticket> ticketList = new ArrayList<>();
            while(rs.next()){
                Ticket ticket = new Ticket(rs);
                ticketList.add(ticket);
            }
            return ticketList;
        } finally {
            connection.close();
        }
    }
}
