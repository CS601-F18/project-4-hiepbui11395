package cs601.project4.servlet;

import com.google.gson.JsonObject;
import cs601.project4.entity.User;
import cs601.project4.utils.Config;
import cs601.project4.utils.HttpUtils;
import cs601.project4.utils.Utils;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class WebFrontEndServlet extends HttpServlet {
    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");
    private final String EVENT_SERVICE_URL = Config.getInstance().getProperty("eventUrl");

    @POST
    @Path("/users/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonRequest) {
        String path = "/create";
        return HttpUtils.callPostRequest(USER_SERVICE_URL, path, jsonRequest);
    }

    @GET
    @Path("/users/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@PathParam("userid") long userId) {
        String path = "/"+userId;
        return HttpUtils.callGetRequest(USER_SERVICE_URL, path);
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
}
