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
import utils.repository.UserRepository;

import javax.ws.rs.core.Response;

public class UserServiceTest {
    private final String USER_SERVICE_URL = Config.getInstance().getProperty("userUrl");
    private UserRepository userRepository = UserRepository.getInstance();

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
        response = HttpUtils.callGetRequest(USER_SERVICE_URL,
                String.format(UserServicePath.GET, userModel.getUserId() + 1));
        Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST_400);
    }

    //TODO: check if /{userid} userid is not valid



    @After
    public void cleanUserTable(){
        userRepository.deleteAll();
    }
}
