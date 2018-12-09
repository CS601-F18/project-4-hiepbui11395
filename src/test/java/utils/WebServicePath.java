package utils;

public class WebServicePath {
    //User path
    public static final String USER_CREATE ="/users/create";

    /**
     * /users/{userid}
     */
    public static final String USER_GET="/users/%s";

    /**
     * /users/{userid}/tickets/transfer
     */
    public static final String USER_TRANSFER_TICKET="/users/%s/tickets/transfer";


    //Event path
    public static final String EVENT_CREATE ="/events/create";

    public static final String EVENT_GET_ALL="/events";

    /**
     * /events/{eventid}
     */
    public static final String EVENT_GET="/events/%s";

    /**
     * /events/{eventid}/purchase/{userid}
     */
    public static final String EVENT_PURCHASE_TICKET="/events/%s/purchase/%s";
}
