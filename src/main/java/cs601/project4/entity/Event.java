package cs601.project4.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Event {
    private Long id;
    private long userId;
    private String name;
    private int numTickets;
    private int numTicketsAvail;

    public Event() {
    }

    public Event(long userId, String name, int numTickets, int numTicketsAvail) {
        this.userId = userId;
        this.name = name;
        this.numTickets = numTickets;
        this.numTicketsAvail = numTicketsAvail;
    }

    public Event(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.userId = rs.getLong("userId");
        this.name = rs.getString("name");
        this.numTickets = rs.getInt("numTickets");
        this.numTicketsAvail = rs.getInt("numTicketsAvail");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public int getNumTicketsAvail() {
        return numTicketsAvail;
    }

    public void setNumTicketsAvail(int numTicketsAvail) {
        this.numTicketsAvail = numTicketsAvail;
    }
}
