package cs601.project4.model;

import cs601.project4.entity.Ticket;

public class TicketModel {
    private long eventid;

    public TicketModel(long eventid) {
        this.eventid = eventid;
    }

    public TicketModel() {
    }

    public TicketModel(Ticket ticket){
        this.eventid = ticket.getEventId();
    }

    public long getEventid() {
        return eventid;
    }

    public void setEventid(long eventid) {
        this.eventid = eventid;
    }
}
