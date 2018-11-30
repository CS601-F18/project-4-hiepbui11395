package cs601.project4.servlet;

import com.google.gson.JsonObject;
import cs601.project4.model.UserModel;
import cs601.project4.utils.Config;
import cs601.project4.utils.HttpUtils;
import cs601.project4.utils.Utils;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
                                   @PathParam("userid") long userid,String jsonRequest){
        JsonObject jsonObject = Utils.toJsonObject(jsonRequest);
        int tickets = jsonObject.get("tickets").getAsInt();
        //TODO: Call user service to add ticket
        String path = "/purchase/"+eventId;
        jsonObject.addProperty("userid", userid);
        jsonObject.addProperty("eventid", eventId);
        return HttpUtils.callPostRequest(EVENT_SERVICE_URL, path, jsonObject.toString());
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
        String userModel = response.readEntity(String.class);
        //Call EventService for event detail
        return Response.ok().build();
    }
}
