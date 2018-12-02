package cs601.project4.model;

import com.google.gson.annotations.SerializedName;
import cs601.project4.entity.Ticket;

public class TicketModel {
    @SerializedName("eventid")
    private long eventId;

    @SerializedName("userid")
    private long userId;

    private int tickets;

    public TicketModel(long eventId) {
        this.eventId = eventId;
    }

    public TicketModel() {
    }

    public TicketModel(Ticket ticket){
        this.eventId = ticket.getEventId();
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventid) {
        this.eventId = eventid;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
