package cs601.project4.servlet;

import com.google.gson.Gson;
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
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class UserServlet {
    private UserService userService = UserService.getInstance();
    private TicketService ticketService = TicketService.getInstance();

    Gson gson = new Gson();

    /**
     * Handle request to create user
     * @param jsonRequest
     * @return
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(String jsonRequest) {
        if(Utils.checkJsonEnoughKey(jsonRequest, new String[]{"username"})) {
            UserModel userModel = Utils.parseJsonToObject(jsonRequest, UserModel.class);
            if (userModel != null) {
                //Check if username/email exist
                User user = userService.findUserByUsername(userModel.getUsername());
                if (user == null && userModel.getUsername() != null) {
                    user = new User(userModel.getUsername());
                    Long id = userService.create(user);
                    if (id == null) {
                        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
                    } else {
                        userModel = new UserModel();
                        userModel.setUserId(id);
                        return Response.ok().entity(gson.toJson(userModel)).build();
                    }
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    /**
     * Handle request to get user from specific user id
     * @param userId
     * @return
     */
    @GET
    @Path("/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("userid") String userId) {
        if(StringUtils.isNumeric(userId)){
            long id = Long.parseLong(userId);
            User user = userService.findUserById(id);
            if (user != null) {
                List<Ticket> ticketList = ticketService.findTicketsByUserId(user.getId());
                UserModel userModel = new UserModel(user, ticketList);
                return Response.ok().entity(gson.toJson(userModel)).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    /**
     * Handle request to add ticket for specific user
     * @param userId
     * @param jsonRequest
     * @return
     */
    @POST
    @Path("/{userid}/tickets/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTicket(@PathParam("userid") String userId, String jsonRequest) {
        if(Utils.checkJsonEnoughKey(jsonRequest, new String[]{"eventid","tickets"})) {
            if (StringUtils.isNumeric(userId)) {
                long id = Long.parseLong(userId);
                //Check userId valid
                User user = userService.findUserById(id);
                if (user != null) {
                    TicketModel ticketModel = Utils.parseJsonToObject(jsonRequest, TicketModel.class);
                    if (ticketModel != null) {
                        //Add tickets to user
                        ticketService.addTicket(id, ticketModel.getEventId(), ticketModel.getTickets());
                        return Response.ok("").build();
                    }
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }

    /**
     * Handle request to transfer ticket from one user to another
     * @param userId
     * @param jsonRequest
     * @return
     */
    @POST
    @Path("/{userid}/tickets/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transferTicket(@PathParam("userid") String userId, String jsonRequest) {
        if(Utils.checkJsonEnoughKey(jsonRequest, new String[]{"eventid","tickets","targetuser"})) {
            if (StringUtils.isNumeric(userId)) {
                long id = Long.parseLong(userId);
                //Check if userId valid
                TicketTransferModel model = Utils.parseJsonToObject(jsonRequest, TicketTransferModel.class);
                if (model != null) {
                    if (ticketService.transferTicket(id, model.getTargetUser(), model.getEventId(), model.getTickets())) {
                        return Response.ok("").build();
                    }
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("").build();
    }
}
