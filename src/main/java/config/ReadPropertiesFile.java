package config;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class ReadPropertiesFile {

    private static final Properties prop = new Properties();
    private static final Map<String, String> cache = new ConcurrentHashMap<>();

    private static String env;
    private static String client;
    private static String userType;

    static {
        loadProperties();
    }

    private ReadPropertiesFile() {
    }

    private static void loadProperties() {
        try {
            InputStream is = ReadPropertiesFile.class.getClassLoader().getResourceAsStream("config.properties");

            if (is == null) {
                throw new RuntimeException("config.properties not found");
            }
            prop.load(is);
            env = System.getProperty("env", "sit").toLowerCase();
            client = System.getProperty("client", "dev").toLowerCase();
            userType = System.getProperty("userType", "admin").toLowerCase();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static String get(ConfigKey key) {
        return get(key.getKey());
    }

    private static String get(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        String value = resolveProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found for key: " + key);
        }
        cache.put(key, value);
        return value;
    }

    private static String resolveProperty(String key) {

        String value = prop.getProperty(env + "." + client + "." + userType + "." + key);
        if (value != null) return value;

        value = prop.getProperty(env + "." + client + "." + key);
        if (value != null) return value;

        value = prop.getProperty(env + "." + key);
        if (value != null) return value;

        return prop.getProperty(key);
    }
}