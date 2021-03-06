package cs601.project4.utils;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class will create a client and make request to other service/api
 */
public class HttpUtils {
    /**
     * Call post request to a service
     * @param url
     * @param path
     * @param jsonRequest
     * @return Response
     */
    public static Response callPostRequest(String url, String path, String jsonRequest) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url + path);
        Invocation.Builder invocationBuilder
                = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(jsonRequest, MediaType.APPLICATION_JSON));
        return response;
    }

    /**
     * Call get request to a service
     * @param url
     * @param path
     * @return Response
     */
    public static Response callGetRequest(String url, String path) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url + path);
        Invocation.Builder invocationBuilder
                = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        return response;
    }
}
