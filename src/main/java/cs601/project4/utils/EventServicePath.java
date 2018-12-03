package cs601.project4.utils;

public class EventServicePath {
    public static final String CREATE ="/create";

    public static final String GET_ALL="/list";

    /**
     * /{eventid}
     */
    public static final String GET="/%s";

    /**
     * /purchase/{eventid}
     */
    public static final String PURCHASE_TICKET="/purchase/%s";

}
