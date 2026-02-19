package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Enterprise Test Data Configuration Manager
 * Centralizes all test data management and provides type-safe access to test
 * datasets
 * 
 * Usage:
 * TestDataConfig config = TestDataConfig.getInstance();
 * TestDataSet dataSet = config.getTestDataSet("kpiLclUcl");
 */
public class TestDataConfig {
    private static TestDataConfig instance;
    private final Map<String, Object> configData;
    private static final String CONFIG_FILE = "testData/api/test-config.yml";
    private static final String DATA_FILE = "testData/api/api-test-data.json";

    private TestDataConfig() throws IOException {
        this.configData = new HashMap<>();
        loadConfig();
    }

    /**
     * Get singleton instance of TestDataConfig
     */
    public static synchronized TestDataConfig getInstance() {
        if (instance == null) {
            try {
                instance = new TestDataConfig();
            } catch (IOException e) {
                throw new RuntimeException("Failed to load test configuration", e);
            }
        }
        return instance;
    }

    /**
     * Load configuration from YAML file
     */
    private void loadConfig() throws IOException {
        InputStream yamlInput = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
        if (yamlInput == null) {
            throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
        }

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        configData.putAll(yamlMapper.readValue(yamlInput, Map.class));
    }

    /**
     * Get environment configuration
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getEnvironmentConfig(String environment) {
        Map<String, Object> environments = (Map<String, Object>) configData.get("environments");
        return environments != null ? (Map<String, Object>) environments.get(environment) : new HashMap<>();
    }

    /**
     * Get API endpoint
     */
    @SuppressWarnings("unchecked")
    public String getApiEndpoint(String endpointKey) {
        Map<String, Object> endpoints = (Map<String, Object>) configData.get("apiEndpoints");
        return endpoints != null ? (String) endpoints.get(endpointKey) : "";
    }

    /**
     * Get test dataset by name
     */
    @SuppressWarnings("unchecked")
    public TestDataSet getTestDataSet(String dataSetName) {
        Map<String, Object> testDataSets = (Map<String, Object>) configData.get("testDataSets");
        if (testDataSets != null && testDataSets.containsKey(dataSetName)) {
            return new TestDataSet((Map<String, Object>) testDataSets.get(dataSetName));
        }
        throw new IllegalArgumentException("Test dataset not found: " + dataSetName);
    }

    /**
     * Get all test dataset names
     */
    @SuppressWarnings("unchecked")
    public List<String> getTestDataSetNames() {
        Map<String, Object> testDataSets = (Map<String, Object>) configData.get("testDataSets");
        return testDataSets != null ? new ArrayList<>(testDataSets.keySet()) : new ArrayList<>();
    }

    /**
     * Get parameterized test scenarios
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getTestScenarios(String scenarioType) {
        Map<String, Object> parameterizedData = (Map<String, Object>) configData.get("parameterizedTestData");
        if (parameterizedData != null) {
            Object scenarios = parameterizedData.get(scenarioType);
            return scenarios instanceof List ? (List<Map<String, Object>>) scenarios : new ArrayList<>();
        }
        return new ArrayList<>();
    }

    /**
     * Wrapper class for Test Dataset access
     */
    public static class TestDataSet {
        private final Map<String, Object> data;

        public TestDataSet(Map<String, Object> data) {
            this.data = data;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Object> getParameters() {
            return (Map<String, Object>) data.get("parameters");
        }

        @SuppressWarnings("unchecked")
        public Map<String, Object> getTimeRange() {
            return (Map<String, Object>) data.get("timeRange");
        }

        public String getName() {
            return (String) data.get("name");
        }

        public String getDescription() {
            return (String) data.get("description");
        }

        public Object get(String key) {
            return data.get(key);
        }

        public Object getParameter(String key) {
            Map<String, Object> params = getParameters();
            return params != null ? params.get(key) : null;
        }
    }

    /**
     * Reset configuration (useful for testing)
     */
    public static void reset() {
        instance = null;
    }
}
