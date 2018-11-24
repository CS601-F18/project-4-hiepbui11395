package cs601.project4.servlet;

import com.google.gson.JsonObject;
import cs601.project4.entity.User;
import cs601.project4.utils.Config;
import cs601.project4.utils.Utils;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class WebFrontEndServlet extends HttpServlet {
    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");

    @POST
    @Path("/users/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(String request) {
        JsonObject jsonObject = Utils.toJsonObject(request);
        String username = jsonObject.get("username").getAsString();
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(USER_SERVICE_URL+"/create");
        Invocation.Builder invocationBuilder
                = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
        return response;
    }
}
