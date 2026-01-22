package page.api;

import base.api.APIBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.trident.playwright.utils.ParseTheTimeFormat;
import com.trident.playwright.utils.ReadPropertiesFile;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GetTimeSeriesDetails extends APIBase {

    @BeforeClass
    public void setup() {
        initApi();
    }

    public static String getTimeSeriesDataAccordingToKPI() throws IOException {
        String equipmentID = ReadPropertiesFile.get("kpiParamDefIds");

        String body = Files.readString(
                Paths.get("src/test/resources/APIRequests/getEquipmentDetails.json"),
                StandardCharsets.UTF_8);
        APIResponse response = request.post("/query/api/kpis/timeseries",
                RequestOptions.create().setData(body.getBytes(StandardCharsets.UTF_8)));
        Assert.assertEquals(response.status(), 200);

        return response.text();
    }

    @Test
    public static Map<String, Double> parseJsonResponse() throws IOException {
        String json = getTimeSeriesDataAccordingToKPI();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        JsonNode dataArray = root.path("equipKpis")
                .path(0)
                .path("kpis")
                .path(0)
                .path("data");
        Map<String, Double> apiValues = new LinkedHashMap<>();
        Iterator<JsonNode> iterator = dataArray.elements();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            // String timestamp = node.get("timestamp").asText();
            // ParseTheTimeFormat.changeTimeFormat(timestamp);
            String gmtTimestamp = node.get("timestamp").asText();
            String istTimestamp = ParseTheTimeFormat.changeTimeFormat(gmtTimestamp);
            JsonNode valueNode = node.get("doubleValue");
            if (!valueNode.isNull()) {
                double value = valueNode.asDouble();
                // apiValues.put(timestamp, value);
                apiValues.put(istTimestamp, value);
            }
        }

        /*
         * for (Map.Entry<String, Double> entry : apiValues.entrySet()) {
         * System.out.println("TimeStamp:- "+entry.getKey() + " : " +
         * "Value:- "+entry.getValue());
         * 
         * }
         */
        // System.out.println(apiValues.size());
        return apiValues;
    }

    @AfterClass
    public void tearDown() {
        closeApi();
    }
}
