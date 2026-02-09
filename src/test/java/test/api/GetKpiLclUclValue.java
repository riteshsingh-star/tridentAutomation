package test.api;

import com.fasterxml.jackson.databind.JsonNode;
import base.api.APIBase;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import utils.Stats;

import java.io.IOException;
import java.util.Map;

public class GetKpiLclUclValue extends APIBase {

    Map<String, String> kpiData;

    @Test
    public void getKpiLclUcl() throws IOException {

        String definitionId = "9";
        String equipmentId = "4248";

        JsonNode kpiNode = GetKpiRequest.getKpiNode(request, definitionId, equipmentId);

        String lclUclType = kpiNode.path("lclUclType").asText(null);
        String lcl = null;
        String ucl = null;

        System.out.println("lclUclType: " + lclUclType);

        kpiData = GetKpiData.getKpiDataUsingMapPojo();
        Assert.assertNotNull(kpiData, "KPI data is null");
        Assert.assertFalse(kpiData.isEmpty(), "No KPI data available");

        double mean = Stats.calculateMean(kpiData);
        double stdDev = Stats.calculateStdDev(kpiData);

        System.out.println("Mean: " + mean);
        System.out.println("Std Dev: " + stdDev);

        String type = lclUclType == null ? "NONE" : lclUclType.toUpperCase();

        switch (type) {
            case "STATS":
                lcl = String.valueOf(Stats.calculateLCL(mean, stdDev));
                ucl = String.valueOf(Stats.calculateUCL(mean, stdDev));
                break;

            case "FIXED":
                lcl = kpiNode.path("lcl").asText(null);
                ucl = kpiNode.path("ucl").asText(null);
                break;

            case "NONE":
                System.out.println("KPI LCL/UCL not applicable");
                break;

            default:
                Assert.fail("Unknown lclUclType: " + lclUclType);
        }

        if (!"NONE".equals(type)) {
            Assert.assertNotNull(lcl, "LCL should not be null");
            Assert.assertNotNull(ucl, "UCL should not be null");
        }

        System.out.println("Final KPI LCL: " + lcl);
        System.out.println("Final KPI UCL: " + ucl);
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (request != null) request.dispose();
        if (playwright != null) playwright.close();
    }
}
