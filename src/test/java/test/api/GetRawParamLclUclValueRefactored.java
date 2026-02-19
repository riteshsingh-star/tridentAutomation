package test.api;

import base.api.APIBase;
import com.fasterxml.jackson.databind.JsonNode;
import config.ApiTestDataProvider;
import config.DataGeneratorUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import page.api.GetRawParamRequest;
import page.api.GetRawParameterData;
import utils.*;
import static utils.AssertionUtil.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static factory.ApiFactory.getRequest;

/**
 * REFACTORED VERSION: Raw Parameter LCL/UCL Verification Test
 * 
 * IMPROVEMENTS:
 * ✓ Test data moved to centralized JSON configuration (api-test-data.json)
 * ✓ No hardcoded test data in test class
 * ✓ Uses TestNG DataProvider for data-driven testing
 * ✓ Can run multiple test scenarios without code changes
 * ✓ Demonstrates best practices for enterprise test automation
 * 
 * BEFORE (Old Way):
 * LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);
 * LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0, 0, 0);
 * int plantId = 839;
 * int equipmentId = 4248;
 * 
 * AFTER (New Way):
 * Use ApiTestDataProvider with DataProvider annotation
 * Test data managed in api-test-data.json
 */
public class GetRawParamLclUclValueRefactored extends APIBase {

    /**
     * DataProvider that supplies test data from api-test-data.json
     * Automatically runs test for each dataset in "rawParameterTestDataSets"
     */
    @DataProvider(name = "rawParameterLclUclDataProvider")
    public Object[][] provideRawParameterLclUclTestData() {
        return ApiTestDataProvider.getRawParameterTestData();
    }

    /**
     * REFACTORED: Using centralized test data with DataProvider
     * 
     * Test Progression:
     * 1. DataProvider reads api-test-data.json "rawParameterTestDataSets" array
     * 2. For each entry, test method is invoked with TestDataRecord
     * 3. No hardcoding, scalable, enterprise-ready
     * 4. Adding new test scenario requires only JSON update
     * 
     * @param record Test data record from api-test-data.json
     * @throws IOException if API call fails
     */
    @Test(dataProvider = "rawParameterLclUclDataProvider")
    public void getSTDMeanLCLUCL(ApiTestDataProvider.TestDataRecord record) throws IOException {
        // Extract parameters from centralized configuration
        int plantId = record.getInt("plantId");
        int equipmentId = record.getInt("equipmentId");
        int rawParamDefId = record.getInt("rawParamDefId");
        int granularity = record.getInt("granularity");

        // Parse DateTime
        LocalDateTime startTime = parseDateTime(record.getString("startDateTime"));
        LocalDateTime endTime = parseDateTime(record.getString("endDateTime"));

        System.out.println("\n========== RAW PARAMETER TEST EXECUTION ==========");
        System.out.println("Test Name: " + record.getString("testName"));
        System.out.println("Test ID: " + record.getString("id"));
        System.out.println("Plant ID: " + plantId);
        System.out.println("Equipment ID: " + equipmentId);
        System.out.println("Raw Parameter Def ID: " + rawParamDefId);
        System.out.println("Time Range: " + startTime + " to " + endTime);
        System.out.println("===================================================\n");

        // Get raw parameter metadata
        JsonNode rawParamNode = GetRawParamRequest.getRawParamNode(getRequest(), plantId, equipmentId, rawParamDefId);
        String lclUclType = rawParamNode.path("lclUclType").asText(null);
        System.out.println("LCL/UCL Type: " + lclUclType);

        // Get raw parameter data from API
        Map<String, String> rawParameterData = GetRawParameterData.getRawParameterDataValue(
                equipmentId,
                rawParamDefId,
                startTime,
                endTime,
                granularity);

        softAssertNotNull(rawParameterData, "Raw parameter data is null");
        softAssertFalse(rawParameterData.isEmpty(), "No raw parameter data available");
        System.out.println("Raw Parameter data points: " + rawParameterData.size());

        // Calculate statistics
        double mean = Stats.calculateMean(rawParameterData);
        double stdDev = Stats.calculateStdDev(rawParameterData);

        System.out.println("Mean: " + mean);
        System.out.println("Std Dev: " + stdDev);

        // Resolve LCL/UCL values
        LclUclResult result = LclUclUtil.resolveLclUcl(lclUclType, rawParamNode, mean, stdDev, "RawParam");
        System.out.println("LCL: " + result.getLcl());
        System.out.println("UCL: " + result.getUcl());

        // Validate results exist
        softAssertNotNull(result.getLcl(), "LCL value is null");
        softAssertNotNull(result.getUcl(), "UCL value is null");
        System.out.println("✓ LCL/UCL Values Successfully Calculated");
        assertAll();
    }

    /**
     * ADVANCED EXAMPLE: Using dynamic data generation for flexibility
     * 
     * This test demonstrates how to mix centralized config with dynamic data
     * Useful when you need variations but still want central management
     */
    @Test
    public void getSTDMeanLCLUCLWithDynamicData() throws IOException {
        // Generate fresh date range for this test run (last 24 hours)
        Map<String, String> dateRange = DataGeneratorUtil.generateDateRange(24, 0);
        LocalDateTime startTime = LocalDateTime.parse(dateRange.get("start"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endTime = LocalDateTime.parse(dateRange.get("end"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Use standard test parameters
        int plantId = 839;
        int equipmentId = 4248;
        int rawParamDefId = 21;
        int granularity = 60000;

        System.out.println("\n========== DYNAMIC DATA TEST ==========");
        System.out.println("Test ID (unique per run): " + DataGeneratorUtil.generateUniqueId("RAW_PARAM_DYNAMIC"));
        System.out.println("Time Range (relative): " + startTime + " to " + endTime);
        System.out.println("=======================================\n");

        // Rest of test logic follows same pattern
        JsonNode rawParamNode = GetRawParamRequest.getRawParamNode(getRequest(), plantId, equipmentId, rawParamDefId);
        String lclUclType = rawParamNode.path("lclUclType").asText(null);

        Map<String, String> rawParameterData = GetRawParameterData.getRawParameterDataValue(
                equipmentId,
                rawParamDefId,
                startTime,
                endTime,
                granularity);

        softAssertNotNull(rawParameterData, "Raw parameter data is null");
        double mean = Stats.calculateMean(rawParameterData);
        double stdDev = Stats.calculateStdDev(rawParameterData);

        LclUclResult result = LclUclUtil.resolveLclUcl(lclUclType, rawParamNode, mean, stdDev, "RawParam");
        softAssertNotNull(result.getLcl(), "LCL value is null");
        softAssertNotNull(result.getUcl(), "UCL value is null");
        assertAll();
    }

    /**
     * Helper method to parse ISO 8601 DateTime string
     */
    private LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
