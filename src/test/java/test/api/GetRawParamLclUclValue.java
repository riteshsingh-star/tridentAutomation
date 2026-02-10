package test.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.Test;
import base.api.APIBase;
import utils.LclUclResult;
import utils.LclUclUtil;
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
        System.out.println("lclUclType: " + lclUclType);

        rawParameterData = GetRawParameterData.getRawParameterDataUsingPojo();
        Assert.assertNotNull(rawParameterData, "Raw parameter data is null");
        Assert.assertFalse(rawParameterData.isEmpty(), "No raw parameter data available");

        double mean = Stats.calculateMean(rawParameterData);
        double stdDev = Stats.calculateStdDev(rawParameterData);

        System.out.println("Mean: " + mean);
        System.out.println("Std Dev: " + stdDev);

        LclUclResult result = LclUclUtil.resolveLclUcl(lclUclType, rawParamNode, mean, stdDev, "RawParam");
    }
}
