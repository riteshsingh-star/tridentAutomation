package config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for generating dynamic test data
 * Provides methods to generate unique test data on-the-fly for each test run
 * Prevents having to modify JSON files between test runs
 * 
 * Usage:
 * String uniqueName = DataGeneratorUtil.generateUniqueTestName("KPI_Test");
 * LocalDateTime testTime = DataGeneratorUtil.generateRecentDateTime(-24); // 24
 * hours ago
 */
public class DataGeneratorUtil {
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(0);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String TIMESTAMP_PATTERN = "yyyyMMddHHmmss";

    /**
     * Generate unique identifier based on timestamp
     */
    public static String generateUniqueId(String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN));
        return prefix + "_" + timestamp + "_" + ID_COUNTER.incrementAndGet();
    }

    /**
     * Generate unique test name with timestamp
     */
    public static String generateUniqueTestName(String baseName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseName + "_" + timestamp;
    }

    /**
     * Generate test data with dynamic values
     * Useful for creating equipment IDs, KPI IDs that may change between
     * environments
     */
    public static Map<String, Object> generateTestDataSet(String templateId, Map<String, Object> overrides) {
        Map<String, Object> testData = new HashMap<>();
        testData.put("testId", generateUniqueId(templateId));
        testData.put("generatedAt", LocalDateTime.now().toString());
        testData.putAll(overrides);
        return testData;
    }

    /**
     * Generate date range for APIs (relative to current time)
     * 
     * @param hoursBack    How many hours back from now to start
     * @param hoursForward How many hours forward from now to end (usually 0 for
     *                     current time)
     */
    public static Map<String, String> generateDateRange(int hoursBack, int hoursForward) {
        LocalDateTime startDateTime = LocalDateTime.now().minusHours(hoursBack);
        LocalDateTime endDateTime = LocalDateTime.now().plusHours(hoursForward);

        Map<String, String> dateRange = new HashMap<>();
        dateRange.put("start", startDateTime.format(DATETIME_FORMATTER));
        dateRange.put("end", endDateTime.format(DATETIME_FORMATTER));
        dateRange.put("startDate", startDateTime.toLocalDate().toString());
        dateRange.put("startTime", startDateTime.toLocalTime().toString());
        dateRange.put("endDate", endDateTime.toLocalDate().toString());
        dateRange.put("endTime", endDateTime.toLocalTime().toString());
        return dateRange;
    }

    /**
     * Generate date range with specific timeline
     */
    public static Map<String, String> generateDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Map<String, String> dateRange = new HashMap<>();
        dateRange.put("start", startDateTime.format(DATETIME_FORMATTER));
        dateRange.put("end", endDateTime.format(DATETIME_FORMATTER));
        dateRange.put("startDate", startDateTime.toLocalDate().toString());
        dateRange.put("startTime", startDateTime.toLocalTime().toString());
        dateRange.put("endDate", endDateTime.toLocalDate().toString());
        dateRange.put("endTime", endDateTime.toLocalTime().toString());
        return dateRange;
    }

    /**
     * Generate random equipment IDs list for parameterized testing
     */
    public static List<Integer> generateEquipmentIds(int count, int startId) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(startId + i);
        }
        return ids;
    }

    /**
     * Generate random KPI IDs list for parameterized testing
     */
    public static List<Integer> generateKpiIds(int count, int startId) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(startId + i);
        }
        return ids;
    }

    /**
     * Generate parametrized test scenario
     */
    public static Map<String, Object> generateTestScenario(String scenarioName,
            List<Integer> equipmentIds,
            List<Integer> kpiIds,
            int expectedDataPoints) {
        Map<String, Object> scenario = new HashMap<>();
        scenario.put("scenarioId", generateUniqueId(scenarioName));
        scenario.put("scenarioName", scenarioName);
        scenario.put("equipmentIds", equipmentIds);
        scenario.put("kpiIds", kpiIds);
        scenario.put("expectedDataPoints", expectedDataPoints);
        scenario.put("createdAt", LocalDateTime.now().toString());
        return scenario;
    }

    /**
     * Reset ID counter (useful for testing)
     */
    public static void resetIdCounter() {
        ID_COUNTER.set(0);
    }

    /**
     * Get current timestamp in ISO format
     */
    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    /**
     * Get timestamp from specified hours back
     */
    public static String getTimestampHoursBack(int hours) {
        return LocalDateTime.now().minusHours(hours).format(DATETIME_FORMATTER);
    }

    /**
     * Convert string timestamp to LocalDateTime
     */
    public static LocalDateTime parseTimestamp(String timestamp) {
        return LocalDateTime.parse(timestamp, DATETIME_FORMATTER);
    }
}
