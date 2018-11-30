package cs601.project4.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;

public class Utils {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Config.getInstance().getProperty("dateFormat"));

    public static String generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }

    public static String hashPassword(String password, String salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 10000, 128);
        byte[] hash = new byte[128];
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return Hex.encodeHexString(hash);
    }

    public static void printJsonResult(PrintWriter pw, JsonObject result){
        pw.print(result);
        pw.flush();
    }

    public static JsonObject toJsonObject(BufferedReader br){
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(br).getAsJsonObject();
        return result;
    }

    public static JsonObject toJsonObject(String json){
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();
        return result;
    }

    //TODO: check json is valid
}
