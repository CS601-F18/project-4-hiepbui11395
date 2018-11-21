package cs601.project4.service;

import cs601.project4.entity.Event;
import cs601.project4.repository.EventRepository;
import cs601.project4.utils.Utils;

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

    public Long addEvent(Event event){
        Long result = null;
        try {
            result = eventRepository.save(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Event findEventById(long id){
        Event result = null;
        try{
            result = eventRepository.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
