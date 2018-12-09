package cs601.project4.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.WeakHashMap;

public class Utils {

    public static final Gson gson = new Gson();

    /**
     * Parse from json string to json object
     * @param json
     * @return
     */
    public static JsonObject toJsonObject(String json) {
        JsonParser parser = new JsonParser();
        return parser.parse(json).getAsJsonObject();
    }

    /**
     * Parse from json string to a class using gson library
     * @param json
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T parseJsonToObject(String json, Class<T> c) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, c);
        } catch (JsonSyntaxException e){
            return null;
        }
    }

    /**
     * Check if jsonObject contain required keys
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

    /**
     * Return an lock object by id
     * Lock object will get from WeakHashMap if available
     * By Using WeakHashMap, lock will be clean when finish method
     * @param locks
     * @param id
     * @return
     */
    public static Object getLockByEntityId(WeakHashMap<Long, Long> locks, long id){
        synchronized (locks){
            Long lock = locks.get(id);
            if(lock==null){
                lock = new Long(id);
                locks.put(id, lock);
            }
            return lock;
        }
    }
}
