package cs601.project4.servlet;

import com.google.gson.JsonObject;
import cs601.project4.entity.Ticket;
import cs601.project4.entity.User;
import cs601.project4.model.UserModel;
import cs601.project4.service.TicketService;
import cs601.project4.service.UserService;
import cs601.project4.utils.Config;
import cs601.project4.utils.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class UserServlet {
    private UserService userService = UserService.getInstance();
    private TicketService ticketService = TicketService.getInstance();
    private final String EVENT_SERVICE_URL = Config.getInstance().getProperty("eventUrl");

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(String jsonRequesst) {
        JsonObject jsonObject = Utils.toJsonObject(jsonRequesst);
        String username = jsonObject.get("username").getAsString();
        //Check if username/email exist
        User user = userService.findUserByUsername(username);
        if(user==null){
            user = new User(username);
            Long id = userService.create(user);
            if(id==null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else{
                JsonObject result =  new JsonObject();
                result.addProperty("userid", id);
                return Response.ok(result.toString()).build();
            }
        } else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") long id){
        User user = userService.findUserById(id);
        if(user != null){
            List<Ticket> ticketList = ticketService.findTicketsByUserId(user.getId());
            UserModel userModel = new UserModel(user, ticketList);
            return Response.status(Response.Status.OK).entity(userModel).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/{userid}/tickets/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTicket(@PathParam("userid") long userId, String jsonRequest){
        //Check userId valid
        User user = userService.findUserById(userId);
        if(user!=null){
            JsonObject jsonObject = Utils.toJsonObject(jsonRequest);
            long eventId = jsonObject.get("eventid").getAsLong();
            int numTickets = jsonObject.get("tickets").getAsInt();

            if(ticketService.addTicket(userId, eventId, numTickets)){
                return Response.ok().build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
