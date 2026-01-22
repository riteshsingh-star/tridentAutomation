package page.api;

import base.api.APIBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.trident.playwright.utils.ParseTheTimeFormat;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static page.api.GetTimeSeriesDetails.getTimeSeriesDataAccordingToKPI;

public class GetCharDataApi extends APIBase {

    @BeforeClass
    public void setup() {
        initApi();
    }
    public static String my() throws IOException {

        String body = Files.readString(
                Paths.get(System.getProperty("user.dir"),
                        "src","test","resources","APIRequests","getEquipmentDetails.json"),
                StandardCharsets.UTF_8
        );
        APIResponse response = request.post(
                "/query/api/kpis/timeseries",
                RequestOptions.create().setData(body)
        );
        Assert.assertEquals(response.status(), 200);
        String responseText = response.text();
        return responseText;
    }

    public static List<String> getTimeSeriesDataAccordingToKPIS() throws IOException {
        String json = getTimeSeriesDataAccordingToKPI();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode dataArray = root.path("equipKpis")
                .path(0)
                .path("kpis")
                .path(0)
                .path("data");
        List<String> apiValues = new ArrayList<>();
        Iterator<JsonNode> iterator = dataArray.elements();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            String gmtTimestamp = node.get("timestamp").asText();

            JsonNode valueNode = node.get("doubleValue");
            if (!valueNode.isNull()) {
                double value = valueNode.asDouble();
                String valueFromAPI = ParseTheTimeFormat.changeTimeFormatAndValue(gmtTimestamp,value);
                apiValues.add(valueFromAPI);
            }
        }
        System.out.println(apiValues.size());
        return apiValues;
    }

    @Test
    public void fetchAndParseTheResponse() throws IOException {
        List<String> json = getTimeSeriesDataAccordingToKPIS();
        System.out.println(json);
    }

    @AfterClass
    public void tearDown() {
        closeApi();
    }
}
