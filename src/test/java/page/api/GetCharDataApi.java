package page.api;

import base.api.APIBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class GetCharDataApi extends APIBase {

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

    public static Set<String> getTimeSeriesDataAccordingToKPIS() throws IOException {
        String json = getTimeSeriesDataAccordingToKPI();
        Set<String> apiValues=fetchApiData(json);
        return apiValues;
    }

    @Test
    public void fetchAndParseTheResponse() throws IOException {
        Set<String> json = getTimeSeriesDataAccordingToKPIS();
        System.out.println(json);
    }

    @AfterClass
    public void tearDown() {
        closeApi();
    }
}
