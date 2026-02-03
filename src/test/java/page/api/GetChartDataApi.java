package page.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import utils.ReadPropertiesFile;
import org.testng.Assert;
import java.io.IOException;
import java.util.Map;


public class GetChartDataApi extends APIBase {

    static String pathURL = ReadPropertiesFile.get("timeSeriesPathURL");
    public static String getTimeSeriesResponseAccordingToKPI() throws IOException {
        APIResponse response = readJsonFileForApiRequestPayload("timeSeriesDataKPI", pathURL);
        Assert.assertEquals(response.status(), 200);
        return response.text();
    }

    public static Map<String, String> getTimeSeriesDataAccordingToKPIS() throws IOException {
        String responseJson = getTimeSeriesResponseAccordingToKPI();
        Map<String, String> apiValues = fetchApiData(responseJson, "equipKpis", 0, "kpis", 0, "data",false);
        //System.out.println("API Response after Formatting: " + apiValues);
        //System.out.println(apiValues.size());
        return apiValues;
    }
}
