package test.api;
import com.fasterxml.jackson.databind.JsonNode;
import base.api.APIBase;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.LclUclResult;
import utils.LclUclUtil;
import utils.Stats;

import java.io.IOException;
import java.util.Map;

public class GetKpiLclUclValue extends APIBase {

    Map<String, String> kpiData;

    @Test
    public void getKpiLclUcl() throws IOException {

        int definitionId = 9;
        int equipmentId = 4249;

        JsonNode kpiNode = GetKpiRequest.getKpiNode(request, definitionId, equipmentId);

        String lclUclType = kpiNode.path("lclUclType").asText(null);
        System.out.println("lclUclType: " + lclUclType);

        kpiData = GetKpiData.getKpiDataUsingMapPojo();
        Assert.assertNotNull(kpiData, "KPI data is null");
        Assert.assertFalse(kpiData.isEmpty(), "No KPI data available");

        double mean = Stats.calculateMean(kpiData);
        double stdDev = Stats.calculateStdDev(kpiData);

        System.out.println("Mean: " + mean);
        System.out.println("Std Dev: " + stdDev);

        LclUclResult result = LclUclUtil.resolveLclUcl(lclUclType, kpiNode, mean, stdDev, "KPI");

    }
    }


