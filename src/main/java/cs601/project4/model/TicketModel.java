package cs601.project4.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import cs601.project4.entity.Ticket;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketModel {
    @SerializedName("eventid")
    private Long eventId;

    @SerializedName("userid")
    private Long userId;

    private Integer tickets;

    public TicketModel(long eventId) {
        this.eventId = eventId;
    }

    public TicketModel() {
    }

    public TicketModel(Ticket ticket) {
        this.eventId = ticket.getEventId();
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getTickets() {
        return tickets;
    }

    public void setTickets(Integer tickets) {
        this.tickets = tickets;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
