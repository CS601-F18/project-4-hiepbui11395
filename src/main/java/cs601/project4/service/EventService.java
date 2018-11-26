package cs601.project4.service;

import cs601.project4.entity.Event;
import cs601.project4.jdbc.ConnectionUtil;
import cs601.project4.repository.EventRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class EventService {
    private EventRepository eventRepository = EventRepository.getInstace();
    private static EventService instance;

    public static synchronized EventService getInstance() {
        if(instance == null){
            instance = new EventService();
        }
        return instance;
    }

    public Long create(Event event){
        try(Connection connection = ConnectionUtil.getMyConnection()) {
            Event result = eventRepository.create(event, connection);
            return result.getId();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Event findEventById(long id){
        try(Connection connection = ConnectionUtil.getMyConnection()) {
            Event result = eventRepository.findById(id, connection);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean buyTicket(long eventId, long userId, int numTickets){
        try(Connection connection = ConnectionUtil.getMyConnection()){
            Event event = eventRepository.findById(eventId,connection);
            if(event != null && event.getNumTicketsAvail()>= numTickets){
                return eventRepository.buyTickets(event, userId, numTickets, connection);
            }
            return false;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
