package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class ReadJsonFile {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T readJson(String resourcePath, Class<T> clazz) {
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new RuntimeException("File not found: " + resourcePath);
            }
            return mapper.readValue(is, clazz);

        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON: " + resourcePath, e);
        }
    }
}
