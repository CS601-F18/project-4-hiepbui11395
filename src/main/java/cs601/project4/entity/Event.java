package cs601.project4.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class Event {
    private Long id;
    private long creatorId;
    private String name;
    private String description;
    private String location;
    private LocalDate date;

    public Event() {
    }

    public Event(long creatorId, String name, String description, String location, LocalDate date) {
        this.creatorId = creatorId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
    }

    public Event(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.creatorId = rs.getLong("creatorId");
        this.name = rs.getString("name");
        this.description = rs.getString("description");
        this.location = rs.getString("location");
        this.date = rs.getDate("date").toLocalDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
