package page.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import utils.ReadPropertiesFile;

import java.io.IOException;
import java.util.Map;

public class GetKpiData extends APIBase {

    static String pathURL = ReadPropertiesFile.get("timeSeriesPathURL");

    public static String getTimeSeriesResponseAccordingToRawParamId() throws IOException {
        APIResponse response = readJsonFileForApiRequestPayload("KPILclUcl",pathURL);
        Assert.assertEquals(response.status(), 200);
        return response.text();
    }

    public static Map<String,String> getKpiDatavalues() throws IOException {
        String responseJson = getTimeSeriesResponseAccordingToRawParamId();
        Map<String, String> apiValues = fetchApiData(responseJson, "equipKpis", 0, "kpis", 0, "data",false);
        //System.out.println(apiValues);
        return apiValues;

    }
}
