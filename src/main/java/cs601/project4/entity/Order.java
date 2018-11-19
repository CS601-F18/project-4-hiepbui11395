package cs601.project4.entity;

import java.util.Date;

public class Order {
    private Long id;
    private long userId;
    private long eventId;
    private float total;
    private Date date;
    private int status;

    public Order() {
    }

    public Order(Long id, long userId, long eventId, float total, Date date, int status) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.total = total;
        this.date = date;
        this.status = status;
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

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
