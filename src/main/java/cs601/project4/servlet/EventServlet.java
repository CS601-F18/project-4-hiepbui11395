package cs601.project4.servlet;

import com.google.gson.JsonObject;
import cs601.project4.entity.Event;
import cs601.project4.service.EventService;
import cs601.project4.utils.Config;
import cs601.project4.utils.HttpUtils;
import cs601.project4.utils.Utils;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class EventServlet extends HttpServlet {
    private EventService eventService = EventService.getInstance();
    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(String jsonRequest) {
        JsonObject jsonObject = Utils.toJsonObject(jsonRequest);
        long userId = jsonObject.get("userid").getAsLong();
        String eventName = jsonObject.get("eventname").getAsString();
        int numTickets = jsonObject.get("numtickets").getAsInt();
        //Check if user exist by calling user service
        String path = "/"+userId;
        Response response = HttpUtils.callGetRequest(USER_SERVICE_URL, path);
        if(response.getStatus()!= HttpStatus.OK_200){
            return response;
        }

        Event event = new Event(userId, eventName, numTickets, numTickets);
        Long id = eventService.create(event);
        if(id == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else{
            JsonObject result =  new JsonObject();
            result.addProperty("eventid", id);
            return Response.ok(result.toString()).build();
        }
    }

    //TODO: change to async
    @POST
    @Path("/purchase/{eventid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response purchaseTickets(@PathParam("eventid") long eventId, String jsonRequest){
        JsonObject jsonObject = Utils.toJsonObject(jsonRequest);
        long userId = jsonObject.get("userid").getAsLong();
        int numTickets = jsonObject.get("tickets").getAsInt();
        boolean result = eventService.buyTicket(eventId, userId, numTickets);
        if(result){
            return Response.ok().build();
        } else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}