package cs601.project4.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cs601.project4.model.EventModel;
import cs601.project4.model.TicketModel;
import cs601.project4.model.UserModel;
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
public class WebFrontEndServlet extends HttpServlet {
    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");
    private final String EVENT_SERVICE_URL = Config.getInstance().getProperty("eventUrl");

    @GET
    @Path("/events")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvents(){
        String path = "/list";
        return HttpUtils.callGetRequest(EVENT_SERVICE_URL, path);
    }

    @GET
    @Path("/events/{eventid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvent(@PathParam("eventid") long eventId){
        String path = "/events/"+eventId;
        return HttpUtils.callGetRequest(EVENT_SERVICE_URL, path);
    }

    @POST
    @Path("/events/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEvent(String jsonRequest) {
        String path = "/create";
        return HttpUtils.callPostRequest(EVENT_SERVICE_URL, path, jsonRequest);
    }

    @POST
    @Path("/events/{eventid}/purchase/{userid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response purchaseTicket(@PathParam("eventid") long eventId,
                                   @PathParam("userid") long userId,String jsonRequest){
        TicketModel ticketModel = Utils.parseJsonToObject(jsonRequest, TicketModel.class);
        //TODO: Call user service to add ticket
        String path = "/purchase/"+eventId;
        ticketModel.setEventId(eventId);
        ticketModel.setUserId(userId);
        return HttpUtils.callPostRequest(EVENT_SERVICE_URL, path, Utils.gson.toJson(ticketModel));
    }

    @POST
    @Path("/users/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonRequest) {
        String path = "/create";
        return HttpUtils.callPostRequest(USER_SERVICE_URL, path, jsonRequest);
    }

    @POST
    @Path("/users/{userid}/tickets/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transferTicket(@PathParam("userid") long userId, String jsonRequest){
        String path = "/" + userId + "/tickets/transfer";
        return HttpUtils.callPostRequest(USER_SERVICE_URL, path, jsonRequest);
    }

    @GET
    @Path("/users/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userid") long userId){
        String path = "/"+userId;
        Response response = HttpUtils.callGetRequest(USER_SERVICE_URL, path);
        if(response.getStatus() == HttpStatus.OK_200) {
            UserModel userModel = response.readEntity(UserModel.class);
            List<EventModel> eventList = new ArrayList<>();
            for(TicketModel ticket : userModel.getTickets()){
                response = this.getEvent(ticket.getEventId());
                if(response.getStatus() == HttpStatus.BAD_REQUEST_400){
                    return Response.status(HttpStatus.BAD_REQUEST_400).entity("").build();
                } else{
                    EventModel eventModel = response.readEntity(EventModel.class);
                    eventList.add(eventModel);
                }
            }
            userModel.setEvents(eventList);
            JsonObject result = new JsonObject();
            result.addProperty("userid", userModel.getUserId());
            result.addProperty("username", userModel.getUsername());
            result.add("tickets", Utils.gson.toJsonTree(userModel.getEvents()));
            return Response.ok(result.toString()).build();
        }
        return Response.status(HttpStatus.BAD_REQUEST_400).entity("").build();
    }
}
