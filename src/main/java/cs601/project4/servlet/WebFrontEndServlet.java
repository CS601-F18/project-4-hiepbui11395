package cs601.project4.servlet;

import com.google.gson.JsonObject;
import cs601.project4.model.EventModel;
import cs601.project4.model.TicketModel;
import cs601.project4.model.UserModel;
import cs601.project4.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Path("/")
public class WebFrontEndServlet extends HttpServlet {
    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");
    private final String EVENT_SERVICE_URL = Config.getInstance().getProperty("eventUrl");
    Logger logger = LogManager.getLogger();

    @GET
    @Path("/events")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvents() {
        logger.info("WebService: /events");
        String path = EventServicePath.GET_ALL;
        return HttpUtils.callGetRequest(EVENT_SERVICE_URL, path);
    }

    @GET
    @Path("/events/{eventid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvent(@PathParam("eventid") String eventId) {
        logger.info("WebService: /events/"+eventId);
        if(StringUtils.isNumeric(eventId)) {
            String path = String.format(EventServicePath.GET, eventId);
            return HttpUtils.callGetRequest(EVENT_SERVICE_URL, path);
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    @POST
    @Path("/events/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEvent(String jsonRequest) {
        logger.info("WebService: /events/create");
        if(Utils.checkJsonEnoughKey(jsonRequest,
                new String[] {"userid","eventname","numtickets"})) {
            String path = EventServicePath.CREATE;
            return HttpUtils.callPostRequest(EVENT_SERVICE_URL, path, jsonRequest);
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    @POST
    @Path("/events/{eventid}/purchase/{userid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response purchaseTicket(@PathParam("eventid") String eventId,
                                   @PathParam("userid") String userId, String jsonRequest) {
        logger.info("WebService: /events/" + eventId + "/purchase/" + userId);
        if(StringUtils.isNumeric(eventId) && StringUtils.isNumeric(userId)
                && Utils.checkJsonEnoughKey(jsonRequest, new String[]{"tickets"})) {
            TicketModel ticketModel = Utils.parseJsonToObject(jsonRequest, TicketModel.class);
            if (ticketModel != null) {
                String path = String.format(EventServicePath.PURCHASE_TICKET, eventId);
                ticketModel.setEventId(Long.parseLong(eventId));
                ticketModel.setUserId(Long.parseLong(userId));
                return HttpUtils.callPostRequest(EVENT_SERVICE_URL, path, Utils.gson.toJson(ticketModel));
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    @POST
    @Path("/users/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonRequest) {
        logger.info("WebService: /users/create");
        if(Utils.checkJsonEnoughKey(jsonRequest,
                new String[]{"username"})) {
            String path = UserServicePath.CREATE;
            return HttpUtils.callPostRequest(USER_SERVICE_URL, path, jsonRequest);
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    @POST
    @Path("/users/{userid}/tickets/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transferTicket(@PathParam("userid") String userId, String jsonRequest) {
        logger.info("WebService: /users/"+userId+"/tickets/transfer");
        if(StringUtils.isNumeric(userId)
                && Utils.checkJsonEnoughKey(jsonRequest, new String[]{"eventid","tickets","targetuser"})) {
            String path = String.format(UserServicePath.TRANSFER_TICKET, userId);
            return HttpUtils.callPostRequest(USER_SERVICE_URL, path, jsonRequest);
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    @GET
    @Path("/users/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userid") String userId) {
        logger.info("/users/"+userId);
        if(StringUtils.isNumeric(userId)) {
            String path = String.format(UserServicePath.GET, userId);
            Response response = HttpUtils.callGetRequest(USER_SERVICE_URL, path);
            if (response.getStatus() == HttpStatus.OK_200) {
                UserModel userModel = Utils.parseJsonToObject(
                        response.readEntity(String.class), UserModel.class);
                HashMap<Long, EventModel> eventModelHashMap = new HashMap<>();
                List<EventModel> eventList = new ArrayList<>();
                for (TicketModel ticket : userModel.getTickets()) {
                    EventModel eventModel = eventModelHashMap.get(ticket.getEventId());
                    if(eventModel == null) {
                        response = this.getEvent(String.valueOf(ticket.getEventId()));
                        if (response.getStatus() == HttpStatus.BAD_REQUEST_400) {
                            return response;
                        } else {
                            eventModel = Utils.parseJsonToObject(
                                    response.readEntity(String.class), EventModel.class);
                            eventModelHashMap.put(eventModel.getEventid(), eventModel);
                        }
                    }
                    eventList.add(eventModel);
                }
                userModel.setEvents(eventList);
                JsonObject result = new JsonObject();
                result.addProperty("userid", userModel.getUserId());
                result.addProperty("username", userModel.getUsername());
                result.add("tickets", Utils.gson.toJsonTree(userModel.getEvents()));
                return Response.ok(result.toString()).build();
            }
        }
        return Response.status(HttpStatus.BAD_REQUEST_400).entity("").build();
    }
}
