package cs601.project4.service;

import cs601.project4.entity.Ticket;
import cs601.project4.repository.TicketRepository;

import java.sql.SQLException;
import java.util.List;

public class TicketService {
    TicketRepository ticketRepository = TicketRepository.getInstance();

    private static TicketService instance;

    private TicketService(){}

    public static synchronized TicketService getInstance() {
        if(instance == null){
            instance = new TicketService();
        }
        return instance;
    }

    public List<Ticket> findTicketsByUserId(long userId){
        try {
            List<Ticket> ticketList = ticketRepository.findTicketsByUserId(userId);
            return ticketList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
