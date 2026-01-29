package utils;

import java.io.InputStream;
import java.util.Properties;

public class ReadPropertiesFile {
        private static Properties prop;

        public static Properties getProperties() {

            if (prop == null) {
                try {
                    prop = new Properties();
                    InputStream is = ReadPropertiesFile.class
                            .getClassLoader()
                            .getResourceAsStream("config.properties");

                    if (is == null) {
                        throw new RuntimeException("config.properties not found in classpath");
                    }

                    prop.load(is);

                } catch (Exception e) {
                    throw new RuntimeException("Failed to load config.properties", e);
                }
            }
            return prop;
        }

        public static String get(String key) {
            return getProperties().getProperty(key);
        }
}
