package cs601.project4.service;

import com.google.gson.JsonObject;
import cs601.project4.entity.Event;
import cs601.project4.repository.EventRepository;
import cs601.project4.utils.Config;
import cs601.project4.utils.HttpUtils;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.core.Response;
import java.sql.SQLException;

public class EventService {
    private EventRepository eventRepository = EventRepository.getInstace();
    private static EventService instance;

    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");

    public static synchronized EventService getInstance() {
        if(instance == null){
            instance = new EventService();
        }
        return instance;
    }

    public Long create(Event event){
        try {
            Event result = eventRepository.create(event);
            return result.getId();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Event findById(long id){
        try{
            Event result = eventRepository.findById(id);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean buyTicket(long eventId, long userId, int numTickets){
        try {
            Event event = eventRepository.findById(eventId);
            if(event != null && event.getNumTicketsAvail()>= numTickets){
                eventRepository.updateAvailableTicket(event, numTickets);
            }
            String path = "/" + userId + "/tickets/add";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("eventid", eventId);
            jsonObject.addProperty("tickets", numTickets);
            Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, path, jsonObject.toString());
            if(response.getStatus()== HttpStatus.OK_200){
                return true;
            }
            eventRepository.updateAvailableTicket(event, -numTickets);
            return false;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
