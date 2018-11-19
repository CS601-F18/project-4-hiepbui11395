package cs601.project4.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties instance;

    private Config() {}

    public static synchronized Properties getInstance() {
        if (instance == null) {
            InputStream is = null;
            try {
                instance = new Properties();
                is = new FileInputStream("config.properties");
                instance.load(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
