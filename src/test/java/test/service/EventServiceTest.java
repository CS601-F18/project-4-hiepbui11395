package test.service;

import com.google.gson.JsonObject;
import cs601.project4.entity.Event;
import cs601.project4.model.EventModel;
import cs601.project4.model.UserModel;
import cs601.project4.utils.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.repository.EventRepository;
import utils.repository.TicketRepository;
import utils.repository.UserRepository;

import javax.ws.rs.core.Response;

public class EventServiceTest {
    private final String EVENT_SERVICE_URL = Config.getInstance().getProperty("eventUrl");
    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");

    EventRepository eventRepository = EventRepository.getInstance();
    TicketRepository ticketRepository = TicketRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();

    @Test
    public void createEvent_Valid_get200(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
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
        Response response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
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
        Response response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", 1);
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", 1);
        request.addProperty("eventname", "Event A");
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void getList_HasEvent_get200(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(eventRepository.count(), 1);

        response = HttpUtils.callGetRequest(EVENT_SERVICE_URL, EventServicePath.GET_ALL);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void getList_NoEvent_get400(){
        Response response = HttpUtils.callGetRequest(EVENT_SERVICE_URL, EventServicePath.GET_ALL);
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void getEvent_ValidId_get200(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(eventRepository.count(), 1);

        response = HttpUtils.callGetRequest(EVENT_SERVICE_URL,
                String.format(EventServicePath.GET, eventModel.getEventid()));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void getEvent_IncorrectId_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(eventRepository.count(), 1);

        response = HttpUtils.callGetRequest(EVENT_SERVICE_URL,
                String.format(EventServicePath.GET, eventModel.getEventid() + 1));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void getEvent_InvalidId_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);
        Assert.assertEquals(eventRepository.count(), 1);

        response = HttpUtils.callGetRequest(EVENT_SERVICE_URL,
                String.format(EventServicePath.GET, "abc"));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void purchaseTicket_Valid_get200(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);

        //Purchase tickets
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventid", eventModel.getEventid());
        request.addProperty("tickets", 5);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL,
                String.format(EventServicePath.PURCHASE_TICKET, eventModel.getEventid()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.OK_200);

        //Exceed amount after buy 5 tickets
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventid", eventModel.getEventid());
        request.addProperty("tickets", 6);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL,
                String.format(EventServicePath.PURCHASE_TICKET, eventModel.getEventid()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void purchaseTicket_InvalidEventId_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event 1
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);

        //Purchase tickets
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventid", eventModel.getEventid());
        request.addProperty("tickets", 5);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL,
                String.format(EventServicePath.PURCHASE_TICKET, "abc"),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void purchaseTicket_IncorrectUserId_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event 1
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);

        //Purchase tickets
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId() +1);
        request.addProperty("eventid", eventModel.getEventid());
        request.addProperty("tickets", 5);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL,
                String.format(EventServicePath.PURCHASE_TICKET, eventModel.getEventid()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void purchaseTicket_IncorrectEventId_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event 1
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel1 = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);


        //Create event 2
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel2 = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);

        //Purchase tickets 1
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventid", eventModel2.getEventid() + 1);
        request.addProperty("tickets", 5);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL,
                String.format(EventServicePath.PURCHASE_TICKET, eventModel2.getEventid() + 1),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);


        //Purchase tickets (different event id in url and in json body)
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventid", eventModel1.getEventid());
        request.addProperty("tickets", 5);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL,
                String.format(EventServicePath.PURCHASE_TICKET, eventModel2.getEventid()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Test
    public void purchaseTicket_ExceedAmount_get400(){
        JsonObject request = new JsonObject();
        //Create user
        request.addProperty("username","userTest");
        Response response = HttpUtils.callPostRequest(USER_SERVICE_URL, UserServicePath.CREATE,
                Utils.gson.toJson(request));
        UserModel userModel = Utils.parseJsonToObject(
                response.readEntity(String.class), UserModel.class);

        //Create event
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventname", "Event A");
        request.addProperty("numtickets", 10);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL, EventServicePath.CREATE,
                Utils.gson.toJson(request));
        EventModel eventModel = Utils.parseJsonToObject(
                response.readEntity(String.class), EventModel.class);

        //Purchase tickets
        request = new JsonObject();
        request.addProperty("userid", userModel.getUserId());
        request.addProperty("eventid", eventModel.getEventid());
        request.addProperty("tickets", 20);
        response = HttpUtils.callPostRequest(EVENT_SERVICE_URL,
                String.format(EventServicePath.PURCHASE_TICKET, eventModel.getEventid()),
                Utils.gson.toJson(request));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    @Before
    public void cleanTable(){
        eventRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }
}
