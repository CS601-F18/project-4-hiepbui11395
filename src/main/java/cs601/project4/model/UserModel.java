package cs601.project4.model;

import com.google.gson.JsonObject;
import cs601.project4.entity.Event;
import cs601.project4.entity.Ticket;
import cs601.project4.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private long userid;
    private String username;
    private List<TicketModel> tickets;
    private List<EventModel> events;

    public UserModel() {
    }

    public UserModel(long userid, String username, List<TicketModel> tickets) {
        this.userid = userid;
        this.username = username;
        this.tickets = tickets;
    }

    public UserModel(User user, List<Ticket> tickets) {
        this.userid = user.getId();
        this.username = user.getUsername();
        this.tickets = new ArrayList<>();
        tickets.forEach(t -> this.tickets.add(new TicketModel(t)));
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<TicketModel> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketModel> tickets) {
        this.tickets = tickets;
    }

    public List<EventModel> getEvents() {
        return events;
    }

    public void setEvents(List<EventModel> events) {
        this.events = events;
    }
}
