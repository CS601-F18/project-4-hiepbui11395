package cs601.project4.servlet;

import cs601.project4.entity.Event;
import cs601.project4.model.EventCreateModel;
import cs601.project4.model.EventModel;
import cs601.project4.model.TicketModel;
import cs601.project4.service.EventService;
import cs601.project4.utils.Config;
import cs601.project4.utils.HttpUtils;
import cs601.project4.utils.UserServicePath;
import cs601.project4.utils.Utils;
import org.apache.commons.lang3.StringUtils;
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
        if(Utils.checkJsonEnoughKey(jsonRequest,new String[] {"userid","eventname","numtickets"})) {
            EventCreateModel model = Utils.parseJsonToObject(jsonRequest, EventCreateModel.class);
            if (model != null) {
                //Check if user exist by calling user service
                String path = String.format(UserServicePath.GET, model.getUserId());
                Response response = HttpUtils.callGetRequest(USER_SERVICE_URL, path);
                if (response.getStatus() != HttpStatus.OK_200) {
                    return response;
                }

                Event event = new Event(model.getUserId(), model.getEventName(),
                        model.getNumTickets(), model.getNumTickets());
                Long id = eventService.create(event);
                if (id != null) {
                    EventModel result = new EventModel();
                    result.setEventid(id);
                    return Response.ok(result).build();
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    @POST
    @Path("/purchase/{eventid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response purchaseTickets(@PathParam("eventid") String eventId, String jsonRequest) {
        if(StringUtils.isNumeric(eventId) &&
                Utils.checkJsonEnoughKey(jsonRequest, new String[]{"userid","eventid","tickets"})){
            long id = Long.parseLong(eventId);
            TicketModel ticketModel = Utils.parseJsonToObject(jsonRequest, TicketModel.class);
            if(ticketModel!=null && ticketModel.getEventId() == id) {
                boolean result = eventService.buyTicket(id, ticketModel.getUserId(), ticketModel.getTickets());
                if (result) {
                    return Response.ok("").build();
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    @GET
    @Path("/{eventid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response detail(@PathParam("eventid") String eventId) {
        if(StringUtils.isNumeric(eventId)) {
            long id = Long.parseLong(eventId);
            Event event = eventService.findById(id);
            if (event != null) {
                EventModel eventModel = new EventModel(event);
                return Response.ok().entity(eventModel).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
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
        if(eventModelList.size()!=0) {
            return Response.ok().entity(eventModelList).build();
        } else{
            return Response.status(HttpStatus.BAD_REQUEST_400).entity("").build();
        }
    }
}
