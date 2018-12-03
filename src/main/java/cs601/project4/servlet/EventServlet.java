package cs601.project4.servlet;

import com.google.gson.JsonObject;
import cs601.project4.entity.Event;
import cs601.project4.model.EventCreateModel;
import cs601.project4.model.EventModel;
import cs601.project4.model.TicketModel;
import cs601.project4.service.EventService;
import cs601.project4.utils.Config;
import cs601.project4.utils.HttpUtils;
import cs601.project4.utils.Utils;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class EventServlet extends HttpServlet {
    private EventService eventService = EventService.getInstance();
    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(String jsonRequest) {
        EventCreateModel model = Utils.parseJsonToObject(jsonRequest, EventCreateModel.class);
        //Check if user exist by calling user service
        String path = "/" + model.getUserId();
        Response response = HttpUtils.callGetRequest(USER_SERVICE_URL, path);
        if (response.getStatus() != HttpStatus.OK_200) {
            return response;
        }

        Event event = new Event(model.getUserId(), model.getEventName(),
                model.getNumTickets(), model.getNumTickets());
        Long id = eventService.create(event);
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("").build();
        } else {
            JsonObject result = new JsonObject();
            result.addProperty("eventid", id);
            return Response.ok(result.toString()).build();
        }
    }

    //TODO: change to async
    @POST
    @Path("/purchase/{eventid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response purchaseTickets(@PathParam("eventid") long eventId, String jsonRequest) {
        TicketModel ticketModel = Utils.parseJsonToObject(jsonRequest, TicketModel.class);
        boolean result = eventService.buyTicket(eventId, ticketModel.getUserId(), ticketModel.getTickets());
        if (result) {
            return Response.ok("").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("").build();
        }
    }

    @GET
    @Path("/events/{eventid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response detail(@PathParam("eventid") long eventId) {
        Event event = eventService.findById(eventId);
        if (event != null) {
            EventModel eventModel = new EventModel(event);
            return Response.ok().entity(eventModel).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("").build();
        }
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvents() {
        List<Event> eventList = eventService.gets();
        List<EventModel> eventModelList = new ArrayList<>();
        for (int i = 0; i < eventList.size(); i++) {
            eventModelList.add(new EventModel(eventList.get(i)));
        }
        return Response.ok().entity(eventModelList).build();
    }
}
