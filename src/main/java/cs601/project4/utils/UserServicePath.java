package cs601.project4.utils;

public class UserServicePath {
    public static final String CREATE ="/create";

    /**
     * /{userid}
     */
    public static final String GET="/%s";

    /**
     * /{userid}/tickets/add
     */
    public static final String ADD_TICKET="/%s/tickets/add";

    /**
     * /{userid}/tickets/transfer
     */
    public static final String TRANSFER_TICKET="/%s/tickets/transfer";
}
