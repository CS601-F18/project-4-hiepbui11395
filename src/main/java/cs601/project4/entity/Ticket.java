package cs601.project4.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ticket {
    private Long id;
    private long eventId;
    private long userId;

    public Ticket() {
    }

    public Ticket(long eventId, long userId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    public Ticket(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.eventId = rs.getLong("eventId");
        this.userId = rs.getLong("userId");
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
}
