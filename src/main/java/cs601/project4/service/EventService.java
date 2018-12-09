package cs601.project4.service;

import com.google.gson.JsonObject;
import cs601.project4.entity.Event;
import cs601.project4.repository.EventRepository;
import cs601.project4.utils.Config;
import cs601.project4.utils.HttpUtils;
import cs601.project4.utils.UserServicePath;
import cs601.project4.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.WeakHashMap;

public class EventService {

    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");
    private EventRepository eventRepository = EventRepository.getInstace();

    private WeakHashMap<Long, Long> locks = new WeakHashMap<>();

    Logger logger = LogManager.getLogger();

    private static EventService instance;

    private EventService(){}

    public static synchronized EventService getInstance() {
        if (instance == null) {
            instance = new EventService();
        }
        return instance;
    }

    /**
     * Crete event
     * @param event
     * @return EventId or null
     */
    public Long create(Event event) {
        Event result = eventRepository.create(event);
        return result.getId();
    }

    /**
     * Get event by id
     * @param id
     * @return event entity or null(not found)
     */
    public Event findById(long id) {
        Event result = eventRepository.findById(id);
        return result;
    }

    /**
     * Buy ticket for specific event
     * @param eventId
     * @param userId
     * @param numTickets
     * @return
     */
    public boolean buyTicket(long eventId, long userId, int numTickets) {
        Event event = eventRepository.findById(eventId);
        if (event != null) {
            //Check if event tickets are enough
            synchronized (Utils.getLockByEntityId(this.locks, eventId)) {
                logger.info("Event service: Event Id-"+ eventId +" working");
                if(event.getNumTicketsAvail() < numTickets){
                    return false;
                }
                eventRepository.updateAvailableTicket(event, numTickets);
            }
            String path = String.format(UserServicePath.ADD_TICKET, userId);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("eventid", eventId);
            jsonObject.addProperty("tickets", numTickets);
            Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, path, jsonObject.toString());
            if (response.getStatus() == HttpStatus.OK_200) {
                return true;
            }
            //Roll back the number of available ticket
            synchronized (Utils.getLockByEntityId(this.locks, eventId)) {
                logger.info("Event service: Event Id-"+ eventId +" rollback");
                event = eventRepository.findById(eventId);
                eventRepository.updateAvailableTicket(event, -numTickets);
            }
        }
        return false;
    }

    /**
     * Get list of events
     * @return list events
     */
    public List<Event> gets() {
        return eventRepository.gets();
    }
}
