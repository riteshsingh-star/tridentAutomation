package config;

import org.testng.annotations.DataProvider;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Enterprise API Test Data Provider for TestNG
 * Provides data-driven testing capabilities with centralized test data
 * management
 * 
 * Usage in Test Classes:
 * 
 * @DataProvider(name = "kpiTestDataProvider")
 *                    public Object[][] getKpiTestData() {
 *                    return ApiTestDataProvider.getKpiTestData();
 *                    }
 * 
 * @Test(dataProvider = "kpiTestDataProvider")
 *                    public void testKpiData(TestDataRecord record) {
 *                    // Use record.get("definitionId"),
 *                    record.get("equipmentId"), etc.
 *                    }
 */
public class ApiTestDataProvider {
    private static final String DATA_FILE = "testData/api/api-test-data.json";
    private static JsonNode testDataJson;

    static {
        try {
            loadTestData();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load API test data", e);
        }
    }

    /**
     * Load test data from JSON file
     */
    private static void loadTestData() throws IOException {
        InputStream inputStream = ApiTestDataProvider.class.getClassLoader().getResourceAsStream(DATA_FILE);
        if (inputStream == null) {
            throw new RuntimeException("Test data file not found: " + DATA_FILE);
        }
        ObjectMapper mapper = new ObjectMapper();
        testDataJson = mapper.readTree(inputStream);
    }

    /**
     * Get KPI test data as 2D Object array for TestNG DataProvider
     */
    public static Object[][] getKpiTestData() {
        JsonNode kpiDataSets = testDataJson.get("kpiTestDataSets");
        Object[][] data = new Object[kpiDataSets.size()][1];

        for (int i = 0; i < kpiDataSets.size(); i++) {
            data[i][0] = new TestDataRecord(kpiDataSets.get(i));
        }
        return data;
    }

    /**
     * Get Raw Parameter test data as 2D Object array for TestNG DataProvider
     */
    public static Object[][] getRawParameterTestData() {
        JsonNode rawDataSets = testDataJson.get("rawParameterTestDataSets");
        Object[][] data = new Object[rawDataSets.size()][1];

        for (int i = 0; i < rawDataSets.size(); i++) {
            data[i][0] = new TestDataRecord(rawDataSets.get(i));
        }
        return data;
    }

    /**
     * Get Aggregate test data as 2D Object array for TestNG DataProvider
     */
    public static Object[][] getAggregateTestData() {
        JsonNode aggregateDataSets = testDataJson.get("aggregateTestDataSets");
        Object[][] data = new Object[aggregateDataSets.size()][1];

        for (int i = 0; i < aggregateDataSets.size(); i++) {
            data[i][0] = new TestDataRecord(aggregateDataSets.get(i));
        }
        return data;
    }

    /**
     * Get specific test data by ID
     */
    public static TestDataRecord getTestDataById(String dataSetType, String testId) {
        JsonNode dataSets = testDataJson.get(dataSetType);
        for (JsonNode dataSet : dataSets) {
            if (testId.equals(dataSet.get("id").asText())) {
                return new TestDataRecord(dataSet);
            }
        }
        throw new IllegalArgumentException("Test data not found for ID: " + testId);
    }

    /**
     * Wrapper class for individual test data record
     */
    public static class TestDataRecord {
        private final JsonNode data;

        public TestDataRecord(JsonNode data) {
            this.data = data;
        }

        public String getString(String key) {
            return data.has(key) ? data.get(key).asText() : null;
        }

        public Integer getInt(String key) {
            return data.has(key) ? data.get(key).asInt() : null;
        }

        public Long getLong(String key) {
            return data.has(key) ? data.get(key).asLong() : null;
        }

        public Double getDouble(String key) {
            return data.has(key) ? data.get(key).asDouble() : null;
        }

        public Boolean getBoolean(String key) {
            return data.has(key) ? data.get(key).asBoolean() : null;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Object> getMap(String key) {
            if (!data.has(key))
                return null;
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(data.get(key), Map.class);
        }

        public Object get(String key) {
            if (!data.has(key))
                return null;
            JsonNode node = data.get(key);
            if (node.isTextual())
                return node.asText();
            if (node.isInt())
                return node.asInt();
            if (node.isLong())
                return node.asLong();
            if (node.isDouble())
                return node.asDouble();
            if (node.isBoolean())
                return node.asBoolean();
            return node;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }
}
