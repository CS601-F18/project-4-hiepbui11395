package cs601.project4.model;

import com.google.gson.annotations.SerializedName;

public class TicketTransferModel {
    @SerializedName("eventid")
    private long eventId;

    private int tickets;

    @SerializedName("targetuser")
    private long targetUser;

    public TicketTransferModel() {
    }

    public TicketTransferModel(long eventId, int tickets, long targetUser) {
        this.eventId = eventId;
        this.tickets = tickets;
        this.targetUser = targetUser;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public long getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(long targetUser) {
        this.targetUser = targetUser;
    }
}
