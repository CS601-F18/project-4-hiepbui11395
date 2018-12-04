package cs601.project4.service;

import cs601.project4.entity.Ticket;
import cs601.project4.entity.User;
import cs601.project4.repository.TicketRepository;
import cs601.project4.repository.UserRepository;

import java.util.List;

public class TicketService {
    TicketRepository ticketRepository = TicketRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();

    private static TicketService instance;

    private TicketService() {
    }

    public static synchronized TicketService getInstance() {
        if (instance == null) {
            instance = new TicketService();
        }
        return instance;
    }

    public List<Ticket> findTicketsByUserId(long userId) {
        List<Ticket> ticketList = ticketRepository.findTicketsByUserId(userId);
        return ticketList;
    }

    public List<Ticket> findTicketsByUserIdAndEventId(long userId, long eventId) {
        List<Ticket> ticketList = ticketRepository.findTicketsByUserIdAndEventId(userId, eventId);
        return ticketList;
    }

    public void addTicket(long userId, long eventId, int numTickets) {
        for (int i = 0; i < numTickets; i++) {
            ticketRepository.create(userId, eventId);
        }
    }

    public synchronized boolean transferTicket(long userId, long targetUserId, long eventId, int numTickets) {
        User user = userRepository.findById(userId);
        User targetUser = userRepository.findById(targetUserId);
        if (user != null && targetUser != null) {
            List<Ticket> ticketList = ticketRepository.findTicketsByUserIdAndEventId(userId, eventId);
            //Check if user have enough ticket to transfer
            if (ticketList.size() >= numTickets) {
                return ticketRepository.transferTicket(ticketList, userId, targetUserId, numTickets);
            }
        }
        return false;
    }
}
