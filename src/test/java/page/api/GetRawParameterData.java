package page.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import com.trident.playwright.utils.ReadPropertiesFile;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Map;

public class GetRawParameterData extends APIBase {

    static String pathURL = ReadPropertiesFile.get("rawParameterPathURL");
    public static String getTimeSeriesDataAccordingToRawParamId() throws IOException {
        APIResponse response = readJsonFileForApiRequestPayload("getRawParameter",pathURL);
        Assert.assertEquals(response.status(), 200);
        return response.text();
    }

    @Test
    public static void getRawParameterDataValues() throws IOException {
        String responseJson = getTimeSeriesDataAccordingToRawParamId();
        Map<String, String> apiValues = fetchApiData(responseJson, "equipments", 0, "rawParameters", 0, "data");
        System.out.println(apiValues);
    }
}
