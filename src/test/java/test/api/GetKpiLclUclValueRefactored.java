package test.api;

import base.api.APIBase;
import com.fasterxml.jackson.databind.JsonNode;
import config.ApiTestDataProvider;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import page.api.GetKpiData;
import page.api.GetKpiRequest;
import utils.*;
import static utils.AssertionUtil.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static factory.ApiFactory.*;
import static page.api.GetStatisticsDataFromAPI.getStatisticsDataOfKPIS;
import static utils.StatisticsComparison.compareStatisticsFromAPIToCalculation;

/**
 * REFACTORED VERSION: KPI LCL/UCL Verification Test
 * 
 * IMPROVEMENTS:
 * ✓ Test data moved to centralized JSON configuration (api-test-data.json)
 * ✓ No hardcoded test data in test class
 * ✓ Uses TestNG DataProvider for data-driven testing
 * ✓ Can run multiple test scenarios without code changes
 * ✓ Easy to add new test cases - just update JSON file
 * ✓ Enterprise-scalable and maintainable
 * 
 * BEFORE (Old Way):
 * int definitionId = 9;
 * int equipmentId = 4249;
 * LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);
 * 
 * AFTER (New Way):
 * Use ApiTestDataProvider with DataProvider annotation
 */
public class GetKpiLclUclValueRefactored extends APIBase {

    /**
     * DataProvider that supplies test data from api-test-data.json
     * When a new dataset is added to JSON, tests automatically run with new data
     * No code changes needed!
     */
    @DataProvider(name = "kpiLclUclDataProvider")
    public Object[][] provideKpiLclUclTestData() {
        return ApiTestDataProvider.getKpiTestData();
    }

    /**
     * REFACTORED: Using centralized test data
     * 
     * Test execution flow:
     * 1. TestNG DataProvider reads api-test-data.json
     * 2. For each entry in "kpiTestDataSets", this test runs once
     * 3. Test uses TestDataRecord to access all parameters
     * 4. No hardcoding, no JSON modifications needed between runs
     */
    @Test(dataProvider = "kpiLclUclDataProvider")
    public void getKpiLclUcl(ApiTestDataProvider.TestDataRecord record) throws IOException {
        // Extract all parameters from centralized configuration
        int definitionId = record.getInt("definitionId");
        int equipmentId = record.getInt("equipmentId");
        int machineId = record.getInt("machineId");
        int kpiID = record.getInt("kpiID");
        int granularity = record.getInt("granularity");

        // Parse DateTime from centralized config
        LocalDateTime startTime = parseDateTime(record.getString("startDateTime"));
        LocalDateTime endTime = parseDateTime(record.getString("endDateTime"));

        System.out.println("\n========== TEST EXECUTION ==========");
        System.out.println("Test Name: " + record.getString("testName"));
        System.out.println("Test ID: " + record.getString("id"));
        System.out.println("Equipment ID: " + equipmentId);
        System.out.println("Definition ID: " + definitionId);
        System.out.println("Time Range: " + startTime + " to " + endTime);
        System.out.println("====================================\n");

        // Get KPI metadata
        JsonNode kpiNode = GetKpiRequest.getKpiNode(getRequest(), definitionId, equipmentId);
        String lclUclType = kpiNode.path("lclUclType").asText(null);
        System.out.println("LCL/UCL Type: " + lclUclType);

        // Get KPI data from API
        Map<String, String> kpiData = GetKpiData.getKpiDataValue(machineId, kpiID, startTime, endTime, granularity);
        softAssertNotNull(kpiData, "KPI data is null");
        softAssertFalse(kpiData.isEmpty(), "No KPI data available");

        // Calculate statistics
        double mean = Stats.calculateMean(kpiData);
        double stdDev = Stats.calculateStdDev(kpiData);

        System.out.println("Mean: " + mean);
        System.out.println("Std Dev: " + stdDev);

        // Resolve LCL/UCL
        LclUclResult result = LclUclUtil.resolveLclUcl(lclUclType, kpiNode, mean, stdDev, "KPI");

        // Compare with API statistics
        boolean comparison = compareStatisticsFromAPIToCalculation(
                getStatisticsDataOfKPIS(machineId, kpiID, startTime, endTime, granularity),
                Double.parseDouble(result.getLcl()),
                mean,
                stdDev,
                Double.parseDouble(result.getUcl()));

        if (comparison) {
            System.out.println("✓ API Value is Matching From Calculation Value");
        } else {
            System.out.println("✗ API Value is not Matching From Calculation Value");
        }

        softAssertTrue(comparison, "LCL/UCL values do not match expected calculations");
        assertAll();
    }

    /**
     * Helper method to parse ISO 8601 DateTime
     */
    private LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
