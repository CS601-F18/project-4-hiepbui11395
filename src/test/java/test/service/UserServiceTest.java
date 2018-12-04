package test.service;

import com.google.gson.JsonObject;
import cs601.project4.model.UserModel;
import cs601.project4.utils.Config;
import cs601.project4.utils.HttpUtils;
import cs601.project4.utils.UserServicePath;
import cs601.project4.utils.Utils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import utils.repository.TicketRepository;
import utils.repository.UserRepository;

import javax.ws.rs.core.Response;

public class UserServiceTest {
    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");
    private UserRepository userRepository = UserRepository.getInstance();
    private TicketRepository ticketRepository = TicketRepository.getInstance();

    @Test
    public void createUser_ValidUser_get200(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void createUser_MultiValidUser_get200(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest1");
        HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        request.addProperty("username", "userTest2");
        HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        request.addProperty("username", "userTest3");
        HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        int count = userRepository.count();
        Assert.assertEquals(count, 3);
    }

    @Test
    public void createUser_SameUsername_get400(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest");
        HttpUtils.callPostRequest(USER_SERVICE_URL, "/create",
                Utils.gson.toJson(request));
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void getUser_CorrectId_get200(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, "/create",
                Utils.gson.toJson(request));
        UserModel userModel = response.readEntity(UserModel.class);
        response = HttpUtils.callGetRequest(USER_SERVICE_URL,
                String.format(UserServicePath.GET, userModel.getUserId()));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void getUser_IncorrectId_get400(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, "/create",
                Utils.gson.toJson(request));
        UserModel userModel = response.readEntity(UserModel.class);

        //Incorrect userId
        response = HttpUtils.callGetRequest(USER_SERVICE_URL,
                String.format(UserServicePath.GET, userModel.getUserId() + 1));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //Invalid userId
        response = HttpUtils.callGetRequest(USER_SERVICE_URL,
                String.format(UserServicePath.GET, "abc"));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void getUser_InvalidId_get400(){
    }

    @Test
    public void addTicket_Valid_get200(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = response.readEntity(UserModel.class);
        request = new JsonObject();
        request.addProperty("eventid",1);
        request.addProperty("tickets", 10);
        response = HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.ADD_TICKET, userModel.getUserId()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(ticketRepository.count(), 10);
    }

    @Test
    public void addTicket_Invalid_get400(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = response.readEntity(UserModel.class);

        //No eventid field
        request = new JsonObject();
        request.addProperty("tickets", 10);
        response = HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.ADD_TICKET, userModel.getUserId()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //No userId incorrect
        request = new JsonObject();
        request.addProperty("tickets", 10);
        response = HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.ADD_TICKET, "abc"),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //No userId incorrect
        request = new JsonObject();
        request.addProperty("tickets", 10);
        response = HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.ADD_TICKET, userModel.getUserId() + 1),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void transfer_Valid_get200(){
        JsonObject request = new JsonObject();
        //UserFrom
        request.addProperty("username","userTest1");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel1 = response.readEntity(UserModel.class);

        //UserTo
        request.addProperty("username","userTest2");
        response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel2 = response.readEntity(UserModel.class);

        //Create tickets user1
        request = new JsonObject();
        request.addProperty("eventid",1);
        request.addProperty("tickets", 10);
        HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.ADD_TICKET, userModel1.getUserId()),
                Utils.gson.toJson(request));

        //Transfer from user1 to user2
        request = new JsonObject();
        request.addProperty("eventid", 1);
        request.addProperty("tickets",5);
        request.addProperty("targetuser",userModel2.getUserId());
        response = HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.TRANSFER_TICKET, userModel1.getUserId()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(ticketRepository.count(), 10);
    }


    @Test
    public void transfer_Invalid_get400(){
        JsonObject request = new JsonObject();
        //UserFrom
        request.addProperty("username","userTest1");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel1 = response.readEntity(UserModel.class);

        //UserTo
        request.addProperty("username","userTest2");
        response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel2 = response.readEntity(UserModel.class);

        //Create tickets user1
        request = new JsonObject();
        request.addProperty("eventid",1);
        request.addProperty("tickets", 10);
        HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.ADD_TICKET, userModel1.getUserId()),
                Utils.gson.toJson(request));

        //Transfer from invalid user to user2
        request = new JsonObject();
        request.addProperty("eventid", 1);
        request.addProperty("tickets",5);
        request.addProperty("targetuser",userModel2.getUserId());
        response = HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.TRANSFER_TICKET, userModel1.getUserId() + 10),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //Transfer from invalid user to user2
        request = new JsonObject();
        request.addProperty("eventid", 1);
        request.addProperty("tickets",5);
        request.addProperty("targetuser",userModel2.getUserId());
        response = HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.TRANSFER_TICKET, "abc"),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //Transfer from user1 user to invalid user
        request = new JsonObject();
        request.addProperty("eventid", 1);
        request.addProperty("tickets",5);
        request.addProperty("targetuser",userModel2.getUserId() +10);
        response = HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.TRANSFER_TICKET, userModel1.getUserId()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //Transfer from user1 user to user2, out of tickets
        request = new JsonObject();
        request.addProperty("eventid", 1);
        request.addProperty("tickets",15);
        request.addProperty("targetuser",userModel2.getUserId());
        response = HttpUtils.callPostRequest(USER_SERVICE_URL,
                String.format(UserServicePath.TRANSFER_TICKET, userModel1.getUserId()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @After
    public void cleanTable(){
        userRepository.deleteAll();
        ticketRepository.deleteAll();
    }
}
