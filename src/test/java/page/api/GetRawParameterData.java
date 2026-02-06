package page.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import utils.ReadPropertiesFile;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Map;

public class GetRawParameterData extends APIBase {

    static String pathURL = ReadPropertiesFile.get("rawParameterPathURL");

    public static String getTimeSeriesResponseAccordingToRawParamId() throws IOException {
        APIResponse response = readJsonFileForApiRequestPayload("lclUcl",pathURL);
        Assert.assertEquals(response.status(), 200);
        return response.text();
    }

   // @Test
    public static Map<String,String> getKpiDatavalues() throws IOException {
        String responseJson = getTimeSeriesResponseAccordingToRawParamId();
        Map<String, String> apiValues = fetchApiData(responseJson, "equipKpis", 0, "kpis", 0, "data",false);
        //System.out.println(apiValues);
        return apiValues;

    }

    public static Map<String,String> getRawParameterDataValues() throws IOException {
        String responseJson = getTimeSeriesResponseAccordingToRawParamId();
        //System.out.println(responseJson);
        Map<String, String> apiValues =fetchApiData(responseJson,"equipments",0,"rawParameters",0,"data",false);
        //System.out.println(apiValues);
        return apiValues;

    }
}
