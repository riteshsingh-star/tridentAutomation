package test.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import base.api.APIBase;
import utils.ReadPropertiesFile;
import utils.Stats;

import java.io.IOException;
import java.util.Map;

public class GetRawParamLclUclValue extends APIBase {

    Map<String, String> rawParameterData;

    @Test
    public void getSTDMeanLCLUCL() throws IOException {

        int plantId = 839;
        int equipmentId = 4248;
        int rawParamDefId = 21;

        JsonNode rawParamNode = GetRawParamRequest.getRawParamNode(request, plantId, equipmentId, rawParamDefId);

        String lclUclType = rawParamNode.path("lclUclType").asText(null);
        String lcl = null;
        String ucl = null;

        System.out.println("lclUclType: " + lclUclType);

        rawParameterData = GetRawParameterData.getRawParameterDataUsingPojo();
        Assert.assertNotNull(rawParameterData, "Raw parameter data is null");
        Assert.assertFalse(rawParameterData.isEmpty(), "No raw parameter data available");

        double mean = Stats.calculateMean(rawParameterData);
        double stdDev = Stats.calculateStdDev(rawParameterData);

        System.out.println("Mean: " + mean);
        System.out.println("Std Dev: " + stdDev);

        String type = lclUclType == null ? "NONE" : lclUclType.toUpperCase();

        switch (type) {
            case "STATS":
                lcl = String.valueOf(Stats.calculateLCL(mean, stdDev));
                ucl = String.valueOf(Stats.calculateUCL(mean, stdDev));
                break;

            case "FIXED":
                lcl = rawParamNode.path("lcl").asText(null);
                ucl = rawParamNode.path("ucl").asText(null);
                break;

            case "NONE":
                System.out.println("LCL/UCL not applicable");
                break;

            default:
                Assert.fail("Unknown lclUclType: " + lclUclType);
        }

        if (!"NONE".equals(type)) {
            Assert.assertNotNull(lcl, "LCL should not be null");
            Assert.assertNotNull(ucl, "UCL should not be null");
        }

        System.out.println("Final RawParam LCL: " + lcl);
        System.out.println("Final RawParam UCL: " + ucl);
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (request != null) request.dispose();
        if (playwright != null) playwright.close();
    }
}
