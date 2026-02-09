package test.api;

import base.api.APIBase;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import utils.ReadPropertiesFile;
import utils.Stats;

import java.io.IOException;
import java.util.Map;



public class GetKpiLclUclValue extends APIBase {

    Map<String, String> kpiData;

    @Test
    public void getKpiLclUcl() throws IOException {

        String definitionId = "9";
        String equipmentId = "4248";

        String lclUclType = GetKpiRequest.getLclUclType(request, definitionId, equipmentId);

        String lcl = null;
        String ucl = null;

        kpiData = GetKpiData.getKpiDataUsingMapPojo();
        Assert.assertNotNull(kpiData, "KPI data is null");
        Assert.assertFalse(kpiData.isEmpty(), "No KPI data available");
        System.out.println("lclUclType :"+lclUclType);
        double mean = Stats.calculateMean(kpiData);
        double stdDev = Stats.calculateStdDev(kpiData);

        System.out.println("Mean: " + mean);
        System.out.println("Std Dev: " + stdDev);

        String type = lclUclType == null ? "NONE" : lclUclType.toUpperCase();


        switch (type) {
            case "STATS":
                ucl = String.valueOf(Stats.calculateUCL(mean, stdDev));
                lcl = String.valueOf(Stats.calculateLCL(mean, stdDev));
                break;

            case "FIXED":
                lcl = GetKpiRequest.getFixedLcl(request, definitionId, equipmentId);
                ucl = GetKpiRequest.getFixedUcl(request, definitionId, equipmentId);
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
