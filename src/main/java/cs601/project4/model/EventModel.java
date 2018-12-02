package cs601.project4.model;

import com.google.gson.annotations.SerializedName;
import cs601.project4.entity.Event;

public class EventModel {
    @SerializedName("eventid")
    private Long eventId;

    @SerializedName("eventname")
    private String eventName;

    @SerializedName("userid")
    private long userId;

    private int avail;

    private int purchased;

    public EventModel() {
    }

    public EventModel(Long eventId, String eventName, long userId, int avail, int purchased) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.userId = userId;
        this.avail = avail;
        this.purchased = purchased;
    }

    public EventModel(Event event){
        this.eventId = event.getId();
        this.eventName = event.getName();
        this.userId = event.getUserId();
        this.avail = event.getNumTicketsAvail();
        this.purchased = event.getNumTickets() - event.getNumTicketsAvail();
    }

    public Long getEventid() {
        return eventId;
    }

    public void setEventid(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventname) {
        this.eventName = eventname;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getAvail() {
        return avail;
    }

    public void setAvail(int avail) {
        this.avail = avail;
    }

    public int getPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }
}
