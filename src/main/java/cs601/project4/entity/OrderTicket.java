package cs601.project4.entity;

public class OrderTicket {
    private Long id;
    private long ticketId;
    private long orderId;
    private long ownerId;

    public OrderTicket() {
    }

    public OrderTicket(Long id, long ticketId, long orderId, long ownerId) {
        this.id = id;
        this.ticketId = ticketId;
        this.orderId = orderId;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
}
