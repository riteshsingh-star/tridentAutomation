package test.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import utils.ReadPropertiesFile;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

public class GetAggregateValueOfKpi extends APIBase {

    static String pathURL = ReadPropertiesFile.get("timeSeriesPathURL");
    @Test
    public static void getTimeSeriesDataAccordingToRawParamId() throws IOException {
        APIResponse response = readJsonFileForApiRequestPayload("timeSeriesDataKPI",pathURL);
        Assert.assertEquals(response.status(), 200);

        System.out.println(response.text());
    }

    /*@Test
    public static void getRawAggregateValue() throws IOException {
        String responseJson = getTimeSeriesDataAccordingToRawParamId();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseJson);
        Double aggregateValue=getRawParameterValue(rootNode,662,25);
        System.out.println(aggregateValue);
    }*/
}
