package cs601.project4.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import cs601.project4.entity.Event;
import cs601.project4.entity.Ticket;
import cs601.project4.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    @SerializedName("userid")
    private long userId;

    private String username;

    private List<TicketModel> tickets;

    private List<EventModel> events;

    public UserModel() {
    }

    public UserModel(long userId, String username, List<TicketModel> tickets) {
        this.userId = userId;
        this.username = username;
        this.tickets = tickets;
    }

    public UserModel(User user, List<Ticket> tickets) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.tickets = new ArrayList<>();
        tickets.forEach(t -> this.tickets.add(new TicketModel(t)));
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
