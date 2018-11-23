package cs601.project4.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ticket {
    private Long id;
    private long eventId;
    private long userId;
    private String description;
    private int amount;

    public Ticket() {
    }
    public Ticket(long eventId, long userId, float price, String description, int amount) {
        this.eventId = eventId;
        this.userId = userId;
        this.description = description;
        this.amount = amount;
    }

    public Ticket(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.eventId = rs.getLong("eventId");
        this.userId = rs.getLong("userId");
        this.description = rs.getString("description");
        this.amount = rs.getInt("userId");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
