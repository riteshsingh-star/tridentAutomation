package utils;

import java.io.InputStream;
import java.util.Properties;

public class ReadPropertiesFile {
    private static Properties prop;
    private static String env;
    private static String client;
    private static String userType;

    static {
        load();
    }

    private static void load() {
        try {
            prop = new Properties();
            InputStream is = ReadPropertiesFile.class.getClassLoader().getResourceAsStream("config.properties");
            if (is == null) {
                throw new RuntimeException("config.properties not found");
            }
            prop.load(is);
            env = System.getProperty("env", "uat").toLowerCase();
            client = System.getProperty("client", "mtr").toLowerCase();
            userType = System.getProperty("userType", "user").toLowerCase();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static String get(String key) {
        String value = prop.getProperty(
                env + "." + client + "." + userType + "." + key
        );
        if (value != null) return value;

        value = prop.getProperty(
                env + "." + client + "." + key
        );
        if (value != null) return value;

        value = prop.getProperty(
                env + "." + key
        );
        if (value != null) return value;

        return prop.getProperty(key);
    }
}
