package test.service;

import com.google.gson.JsonObject;
import cs601.project4.model.EventModel;
import cs601.project4.model.UserModel;
import cs601.project4.utils.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.WebServicePath;
import utils.repository.EventRepository;
import utils.repository.TicketRepository;
import utils.repository.UserRepository;

import javax.ws.rs.core.Response;

public class WebFrontEndServiceTest {
    private final String WEB_SERVICE_URL = Config.getInstance().getProperty("webUrl");
    private EventRepository eventRepository = EventRepository.getInstance();
    private UserRepository userRepository = UserRepository.getInstance();
    private TicketRepository ticketRepository = TicketRepository.getInstance();

    /**
     * User Service interaction
     */

    @Test
    public void createUser_ValidUser_get200(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void createUser_MultiValidUser_get200(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest1");
        HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        request.addProperty("username", "userTest2");
        HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        request.addProperty("username", "userTest3");
        HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        int count = userRepository.count();
        Assert.assertEquals(count, 3);
    }

    @Test
    public void createUser_SameUsername_get400(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest");
        HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void getUser_CorrectId_get200(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);
        response = HttpUtils.callGetRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.USER_GET, userModel.getUserId()));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void getUser_IncorrectId_get400(){
        JsonObject request = new JsonObject();
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Incorrect userId
        response = HttpUtils.callGetRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.USER_GET, userModel.getUserId() + 1));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //Invalid userId
        response = HttpUtils.callGetRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.USER_GET, "abc"));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    //TODO: Transfer ticket
    @Test
    public void transfer_Valid_get200(){
        JsonObject request = new JsonObject();
        //Create UserFrom
        request.addProperty("username","userTest1");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel1 = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create UserTo
        request.addProperty("username","userTest2");
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel2 = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel1.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);

        //Purchase tickets user1
        request = new JsonObject();
        request.addProperty("tickets", 10);
        HttpUtils.callPostRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.EVENT_PURCHASE_TICKET, eventModel.getEventid(), userModel1.getUserId()),
                Utils.gson.toJson(request));

        //Transfer from user1 to user2
        request = new JsonObject();
        request.addProperty("eventid", eventModel.getEventid());
        request.addProperty("tickets",5);
        request.addProperty("targetuser",userModel2.getUserId());
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.USER_TRANSFER_TICKET, userModel1.getUserId()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(ticketRepository.count(), 10);
    }

    /**
     * Event Service Interaction
     */

    @Test
    public void createEvent_Valid_get200(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(eventRepository.count(), 1);
    }

    @Test
    public void createEvent_WrongUserId_get400(){
        JsonObject request = new JsonObject();
        //Create event
        request = new JsonObject();
        request.addProperty("userid", 1);
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void createEvent_InvalidJson_get400(){
        JsonObject request = new JsonObject();
        //Create event
        request = new JsonObject();
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", 1);
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", 1);
        request.addProperty("eventname", "Event A");
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void getList_HasEvent_get200(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(eventRepository.count(), 1);

        response = HttpUtils.callGetRequest(WEB_SERVICE_URL, WebServicePath.EVENT_GET_ALL);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void getList_NoEvent_get400(){
        Response response = HttpUtils.callGetRequest(WEB_SERVICE_URL, WebServicePath.EVENT_GET_ALL);
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void getEvent_ValidId_get200(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(eventRepository.count(), 1);

        response = HttpUtils.callGetRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.EVENT_GET, eventModel.getEventid()));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void getEvent_IncorrectId_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(eventRepository.count(), 1);

        response = HttpUtils.callGetRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.EVENT_GET, eventModel.getEventid() + 1));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void getEvent_InvalidId_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(eventRepository.count(), 1);

        response = HttpUtils.callGetRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.EVENT_GET, "abc"));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void purchaseTicket_Valid_get200(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);

        //Purchase tickets
        request = new JsonObject();
        request.addProperty("tickets", 5);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.EVENT_PURCHASE_TICKET, eventModel.getEventid(), userModel.getUserId()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);


        response = HttpUtils.callGetRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.USER_GET, userModel.getUserId()));
        userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(userModel.getTickets().size(), 5);
    }

    @Test
    public void purchaseTicket_InvalidEventId_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event 1
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);

        //Purchase tickets
        request = new JsonObject();
        request.addProperty("tickets", 5);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.EVENT_PURCHASE_TICKET, "abc",userModel.getUserId()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void purchaseTicket_IncorrectUserId_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event 1
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = response.readEntity(EventModel.class);

        //Purchase tickets
        request = new JsonObject();
        request.addProperty("tickets", 5);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.EVENT_PURCHASE_TICKET, eventModel.getEventid(), userModel.getUserId()+1),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void purchaseTicket_IncorrectEventId_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event 1
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel1 = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);


        //Create event 2
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel2 = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);

        //Purchase tickets 1
        request = new JsonObject();
        request.addProperty("tickets", 5);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.EVENT_PURCHASE_TICKET, eventModel2.getEventid() + 1, userModel.getUserId()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void purchaseTicket_ExceedAmount_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.USER_CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL, WebServicePath.EVENT_CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);

        //Purchase tickets
        request = new JsonObject();
        request.addProperty("tickets", 20);
        response = HttpUtils.callPostRequest(WEB_SERVICE_URL,
                String.format(WebServicePath.EVENT_PURCHASE_TICKET, eventModel.getEventid(), userModel.getUserId()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Before
    public void cleanTable(){
        eventRepository.deleteAll();
        userRepository.deleteAll();
        ticketRepository.deleteAll();
    }
}
