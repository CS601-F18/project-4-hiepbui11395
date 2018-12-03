package cs601.project4.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Utils {

    public static final Gson gson = new Gson();

    public static void printJsonResult(PrintWriter pw, JsonObject result) {
        pw.print(result);
        pw.flush();
    }

    public static JsonObject toJsonObject(BufferedReader br) {
        JsonParser parser = new JsonParser();
        return parser.parse(br).getAsJsonObject();
    }

    public static JsonObject toJsonObject(String json) {
        JsonParser parser = new JsonParser();
        return parser.parse(json).getAsJsonObject();
    }

    public static <T> T parseJsonToObject(String json, Class<T> c) {
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }

    //TODO: check json is valid
}
