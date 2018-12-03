package cs601.project4.model;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventCreateModel {
    @SerializedName("eventname")
    private String eventName;

    @SerializedName("userid")
    private long userId;

    @SerializedName("numtickets")
    private int numTickets;

    public EventCreateModel() {
    }

    public EventCreateModel(String eventName, long userId, int numTickets) {
        this.eventName = eventName;
        this.userId = userId;
        this.numTickets = numTickets;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }
}
