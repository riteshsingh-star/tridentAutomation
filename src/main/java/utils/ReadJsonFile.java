package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;


/**
 * Utility class for reading JSON files from the test resources folder
 * and mapping them to Java objects using Jackson ObjectMapper.
 * Expected folder structure:
 * src/test/resources/testData/
 * Usage:
 * MyClass data = ReadJsonFile.readJson("sample.json", MyClass.class);
 */

public class ReadJsonFile {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Reads a JSON file from the testData folder
     * and converts it into the specified class type.
     * @param resourcePath File name inside testData folder
     * @param clazz Target class type for mapping
     * @param <T> Generic return type
     * @return Deserialized Java object of type T
     * @throws RuntimeException if file is not found or JSON parsing fails
     */

    public static <T> T readJson(String resourcePath, Class<T> clazz) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("testData/"+resourcePath)) {
            if (is == null) {
                throw new RuntimeException("File not found: " + resourcePath);
            }
            return mapper.readValue(is, clazz);

        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON: " + resourcePath, e);
        }
    }
}
