package cs601.project4.entity;

public class Ticket {
    private Long id;
    private long eventId;
    private float price;
    private String description;
    private int amount;

    public Ticket() {
    }

    public Ticket(Long id, long eventId, float price, String description, int amount) {
        this.id = id;
        this.eventId = eventId;
        this.price = price;
        this.description = description;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
