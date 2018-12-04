package cs601.project4.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

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
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, c);
        } catch (JsonSyntaxException e){
            return null;
        }
    }

    /**
     * Check if jsonObject contain required key
     * @param jsonStr
     * @param keys
     * @return
     */
    public static boolean checkJsonEnoughKey(String jsonStr, String[] keys){
        JsonObject jsonObject = Utils.toJsonObject(jsonStr);
        for(int i=0;i<keys.length;i++){
            if(!jsonObject.has(keys[i])){
                return false;
            }
        }
        return true;
    }
}
