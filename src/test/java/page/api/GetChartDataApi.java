package page.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import com.trident.playwright.utils.ReadPropertiesFile;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;


public class GetChartDataApi extends APIBase {

    static String pathURL = ReadPropertiesFile.get("timeSeriesPathURL");
    public static String getTimeSeriesDataAccordingToKPI() throws IOException {
        APIResponse response = readJsonFileForApiRequestPayload("getEquipmentDetails", pathURL);
        Assert.assertEquals(response.status(), 200);
        return response.text();
    }

    public static Map<String, String> getTimeSeriesDataAccordingToKPIS() throws IOException {
        String responseJson = getTimeSeriesDataAccordingToKPI();
        Map<String, String> apiValues = fetchApiData(responseJson, "equipKpis", 0, "kpis", 0, "data");
        System.out.println("API Response after Formatting: " + apiValues);
        System.out.println(apiValues.size());
        return apiValues;
    }
}
