package test.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;
import org.testng.annotations.Test;
import base.api.APIBase;
import page.api.GetRawParamRequest;
import page.api.GetRawParameterData;
import utils.LclUclResult;
import utils.LclUclUtil;
import utils.Stats;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static factory.ApiFactory.getRequest;
import static utils.AssertionUtil.assertFalse;
import static utils.AssertionUtil.assertNotNull;

public class GetRawParamLclUclValue extends APIBase {

    Map<String, String> rawParameterData;
    LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);
    LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0, 0, 0);
    int granularity = 60000;
    int plantId = 839;
    int equipmentId = 4248;
    int rawParamDefId = 21;

    @Test
    public void getSTDMeanLCLUCL() throws IOException {
        JsonNode rawParamNode = GetRawParamRequest.getRawParamNode(getRequest(), plantId, equipmentId, rawParamDefId);
        String lclUclType = rawParamNode.path("lclUclType").asText(null);
        System.out.println("lclUclType: " + lclUclType);
        rawParameterData = GetRawParameterData.getRawParameterDataValue(equipmentId, rawParamDefId, startTime, endTime, granularity);
        assertNotNull(rawParameterData, "Raw parameter data is null");
        assertFalse(rawParameterData.isEmpty(), "No raw parameter data available");
        double mean = Stats.calculateMean(rawParameterData);
        double stdDev = Stats.calculateStdDev(rawParameterData);

        System.out.println("Mean: " + mean);
        System.out.println("Std Dev: " + stdDev);

        LclUclResult result = LclUclUtil.resolveLclUcl(lclUclType, rawParamNode, mean, stdDev, "RawParam");
    }
}
