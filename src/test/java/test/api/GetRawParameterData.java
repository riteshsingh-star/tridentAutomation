package test.api;

import base.api.APIBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import pojo.api.*;
import utils.ReadPropertiesFile;
import org.testng.Assert;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRawParameterData extends APIBase {

    static String pathURL = ReadPropertiesFile.get("rawParameterPathURL");

    public static String getRawParameterDataa() {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> equipments = new HashMap<>();
        equipments.put("id", 4248);
        equipments.put("rawParamDefIds", List.of(21));
        payload.put("equipments", List.of(equipments));
        Map<String, Object> dateRange = new HashMap<>();
        dateRange.put("startTs", "2026-01-28T04:30:00.000Z");
        dateRange.put("endTs", "2026-01-29T04:30:00.000Z");
        payload.put("dateRange", dateRange);
        payload.put("granularityInMillis", 60000L);
        payload.put("addMissingTimestamps", true);
        APIResponse response = postApiRequest(payload, pathURL);
        return response.text();
    }

    public static Map<String, String> getRawParameterDataUsingMap() throws IOException {
        String responseJson = getRawParameterDataa();
        Map<String, String> apiValues = fetchApiData(responseJson, "equipments", 0, "rawParameters", 0, "data", false);
        return apiValues;

    }

    public static String GetRawParaDataPojo() {
        Raws rawdata = new Raws(4248, List.of(21));

        DateRange dateRange = new DateRange(
                "2026-01-26T04:30:00.000Z",
                "2026-01-27T04:30:00.000Z");

        RawRequest request = new RawRequest(List.of(rawData), dateRange, 0, true);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payload = mapper.convertValue(request, Map.class);
        APIResponse response = postApiRequest(payload, pathURL);
        System.out.println(response.text());
        Assert.assertEquals(response.status(), 200);
        return response.text();
    }

    public static Map<String, String> getRawParameterDataUsingPojo() throws IOException {
        String responseJson = getRawParaDataPojo();
        Map<String, String> apiValues = fetchApiData(responseJson, "equipments", 0, "rawParameters", 0, "data", false);
        return apiValues;
    }
}