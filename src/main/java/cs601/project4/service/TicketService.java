package cs601.project4.service;

import cs601.project4.entity.Ticket;
import cs601.project4.entity.User;
import cs601.project4.repository.TicketRepository;
import cs601.project4.repository.UserRepository;
import cs601.project4.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TicketService {
    TicketRepository ticketRepository = TicketRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();

    private WeakHashMap<Long, Long> locks = new WeakHashMap<>();

    Logger logger = LogManager.getLogger();

    private static TicketService instance;

    private TicketService() { }

    public static synchronized TicketService getInstance() {
        if (instance == null) {
            instance = new TicketService();
        }
        return instance;
    }

    /**
     * Get list ticket by userId
     * @param userId
     * @return list tickets
     */
    public List<Ticket> findTicketsByUserId(long userId) {
        List<Ticket> ticketList = ticketRepository.findTicketsByUserId(userId);
        return ticketList;
    }

    /**
     * Add tickets into db
     * @param userId
     * @param eventId
     * @param numTickets
     */
    public void addTicket(long userId, long eventId, int numTickets) {
        for (int i = 0; i < numTickets; i++) {
            ticketRepository.create(userId, eventId);
        }
    }

    /**
     * Transfer ticket from one user to another
     * @param userId
     * @param targetUserId
     * @param eventId
     * @param numTickets
     * @return Result of transaction
     */
    public boolean transferTicket(long userId, long targetUserId, long eventId, int numTickets) {
        User user = userRepository.findById(userId);
        User targetUser = userRepository.findById(targetUserId);
        if (user != null && targetUser != null) {
            synchronized (Utils.getLockByEntityId(this.locks, userId)) {
                logger.info("Ticket Service: User-" + userId + ": start!");
                List<Ticket> ticketList = ticketRepository.findTicketsByUserIdAndEventId(userId, eventId);
                //Check if user have enough ticket to transfer
                if (ticketList.size() >= numTickets) {
                    return ticketRepository.transferTicket(ticketList, userId, targetUserId, numTickets);
                }
            }
        }
        return false;
    }
}
