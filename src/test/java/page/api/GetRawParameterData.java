package page.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import utils.ReadPropertiesFile;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRawParameterData extends APIBase {

    static String pathURL = ReadPropertiesFile.get("rawParameterPathURL");

    public static String getTimeSeriesResponseAccordingToRawParamId() throws IOException {
        APIResponse response = readJsonFileForApiRequestPayload("rawParamlclUcl", pathURL);
        Assert.assertEquals(response.status(), 200);
        return response.text();
    }

    public static Map<String, String> getRawParameterDataValues() throws IOException {
        String responseJson = getTimeSeriesResponseAccordingToRawParamId();
        //System.out.println(responseJson);
        Map<String, String> apiValues = fetchApiData(responseJson, "equipments", 0, "rawParameters", 0, "data", false);
        //System.out.println(apiValues);
        return apiValues;

    }


    public static String getRawParameterData() {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> equipments = new HashMap<>();
        equipments.put("id", 4248);
        equipments.put("rawParamDefIds", List.of(21));
        payload.put("equipments", List.of(equipments));
        Map<String, Object> dateRange = new HashMap<>();
        dateRange.put("startTs", "2026-01-28T04:30:00.000Z");
        dateRange.put("endTs", "2026-01-29T04:30:00.000Z");
        payload.put("dateRange", dateRange);
        payload.put("granularityInMillis", 0L);
        payload.put("addMissingTimestamps", true);
        APIResponse response = postApiRequest(payload, pathURL);
        return response.text();
    }


    public static Map<String, String> getRawParameterDataUsingMap() throws IOException {
        String responseJson=getRawParameterData();
        Map<String, String> apiValues = fetchApiData(responseJson, "equipments", 0, "rawParameters", 0, "data", false);
        return apiValues;

    }
}