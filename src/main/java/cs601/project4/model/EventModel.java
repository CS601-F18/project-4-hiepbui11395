package cs601.project4.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import cs601.project4.entity.Event;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventModel {
    @SerializedName("eventid")
    private Long eventId;

    @SerializedName("eventname")
    private String eventName;

    @SerializedName("userid")
    private Long userId;

    private Integer avail;

    private Integer purchased;

    public EventModel() {
    }

    public EventModel(Long eventId, String eventName, Long userId, int avail, int purchased) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.userId = userId;
        this.avail = avail;
        this.purchased = purchased;
    }

    public EventModel(Event event) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAvail() {
        return avail;
    }

    public void setAvail(Integer avail) {
        this.avail = avail;
    }

    public Integer getPurchased() {
        return purchased;
    }

    public void setPurchased(Integer purchased) {
        this.purchased = purchased;
    }
}
