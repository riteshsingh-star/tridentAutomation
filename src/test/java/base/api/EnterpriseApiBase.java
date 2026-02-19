package base.api;

import config.TestDataConfig;
import org.testng.annotations.BeforeSuite;
import java.util.Map;

/**
 * Enterprise Base API Test Class with Data Management
 * 
 * Provides:
 * - Centralized configuration access
 * - Consistent test setup/teardown
 * - Ready for multi-environment testing
 * - Data provider utilities
 * 
 * Usage:
 * public class MyApiTest extends EnterpriseApiBase {
 * 
 * @Test
 *       public void myTest() {
 *       String baseUrl = getEnvironmentBaseUrl("sit");
 *       String endpoint = getApiEndpoint("kpiTimeseries");
 *       // Test logic
 *       }
 *       }
 */
public class EnterpriseApiBase extends APIBase {
    protected TestDataConfig testDataConfig;
    protected String currentEnvironment;

    @BeforeSuite
    public void initializeTestConfiguration() {
        // Initialize centralized configuration
        testDataConfig = TestDataConfig.getInstance();

        // Get environment from system property or use default
        currentEnvironment = System.getProperty("environment", "sit");

        System.out.println("[TEST CONFIG] Environment: " + currentEnvironment);
        System.out.println("[TEST CONFIG] Configuration loaded successfully");
    }

    /**
     * Get base URL for current environment
     */
    protected String getEnvironmentBaseUrl(String environment) {
        @SuppressWarnings("unchecked")
        Map<String, Object> envConfig = (Map<String, Object>) testDataConfig.getEnvironmentConfig(environment)
                .get("baseUrl");
        return envConfig != null ? (String) envConfig.get("baseUrl") : null;
    }

    /**
     * Get API base URL for current environment
     */
    protected String getEnvironmentApiBaseUrl(String environment) {
        @SuppressWarnings("unchecked")
        Map<String, Object> envConfig = testDataConfig.getEnvironmentConfig(environment);
        return envConfig != null ? (String) envConfig.get("apiBaseUrl") : null;
    }

    /**
     * Get API endpoint
     */
    protected String getApiEndpoint(String endpointKey) {
        return testDataConfig.getApiEndpoint(endpointKey);
    }

    /**
     * Get full API URL (baseUrl + endpoint)
     */
    protected String getFullApiUrl(String environment, String endpointKey) {
        String baseUrl = getEnvironmentApiBaseUrl(environment);
        String endpoint = getApiEndpoint(endpointKey);
        return baseUrl + endpoint;
    }

    /**
     * Get current environment
     */
    protected String getCurrentEnvironment() {
        return currentEnvironment;
    }

    /**
     * Check if running against specific environment
     */
    protected boolean isEnvironment(String environment) {
        return currentEnvironment.equalsIgnoreCase(environment);
    }
}
