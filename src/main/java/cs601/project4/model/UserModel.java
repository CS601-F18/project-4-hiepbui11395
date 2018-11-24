package cs601.project4.model;

import cs601.project4.entity.Ticket;
import cs601.project4.entity.User;

import java.util.List;

public class UserModel {
    private long userId;
    private String username;
    private List<Ticket> tickets;

    public UserModel() {
    }

    public UserModel(long userId, String username, List<Ticket> tickets) {
        this.userId = userId;
        this.username = username;
        this.tickets = tickets;
    }

    public UserModel(User user, List<Ticket> tickets) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.tickets = tickets;
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

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
