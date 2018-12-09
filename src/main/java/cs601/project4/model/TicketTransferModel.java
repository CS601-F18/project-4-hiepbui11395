package cs601.project4.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketTransferModel {
    @SerializedName("eventid")
    private Long eventId;

    private Integer tickets;

    @SerializedName("targetuser")
    private Long targetUser;

    public TicketTransferModel() {
    }

    public TicketTransferModel(Long eventId, int tickets, Long targetUser) {
        this.eventId = eventId;
        this.tickets = tickets;
        this.targetUser = targetUser;
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

    public Long getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(Long targetUser) {
        this.targetUser = targetUser;
    }
}
