package cs601.project4.servlet;

import com.google.gson.JsonObject;
import cs601.project4.entity.Ticket;
import cs601.project4.entity.User;
import cs601.project4.model.TicketModel;
import cs601.project4.model.TicketTransferModel;
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
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(String jsonRequest) {
        UserModel userModel = Utils.parseJsonToObject(jsonRequest, UserModel.class);
        //Check if username/email exist
        User user = userService.findUserByUsername(userModel.getUsername());
        if (user == null) {
            user = new User(userModel.getUsername());
            Long id = userService.create(user);
            if (id == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("").build();
            } else {
                JsonObject result = new JsonObject();
                result.addProperty("userid", id);
                return Response.ok(result.toString()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("").build();
        }
    }

    @GET
    @Path("/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("userid") long id) {
        User user = userService.findUserById(id);
        if (user != null) {
            List<Ticket> ticketList = ticketService.findTicketsByUserId(user.getId());
            UserModel userModel = new UserModel(user, ticketList);
            return Response.status(Response.Status.OK).entity(userModel).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("").build();
        }
    }

    @POST
    @Path("/{userid}/tickets/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTicket(@PathParam("userid") long userId, String jsonRequest) {
        //Check userId valid
        User user = userService.findUserById(userId);
        if (user != null) {
            TicketModel ticketModel = Utils.parseJsonToObject(jsonRequest, TicketModel.class);
            //Add tickets to user
            ticketService.addTicket(userId, ticketModel.getEventId(), ticketModel.getTickets());
            return Response.ok("").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    @POST
    @Path("/{userid}/tickets/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transferTicket(@PathParam("userid") long userId, String jsonRequest) {
        //Check if userId valid
        TicketTransferModel model = Utils.parseJsonToObject(jsonRequest, TicketTransferModel.class);
        if (ticketService.transferTicket(userId, model.getTargetUser(), model.getEventId(), model.getTickets())) {
            return Response.ok("").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }
}
