package test.api;

import com.fasterxml.jackson.databind.JsonNode;
import base.api.APIBase;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.api.GetKpiData;
import page.api.GetKpiRequest;
import utils.LclUclResult;
import utils.LclUclUtil;
import utils.Stats;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static factory.ApiFactory.*;
import static page.api.GetStatisticsDataFromAPI.getStatisticsDataOfKPIS;
import static utils.StatisticsComparison.compareStatisticsFromAPIToCalculation;

public class GetKpiLclUclValue extends APIBase {

    Map<String, String> kpiData;

    int definitionId = 9;
    int equipmentId = 4249;
    LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);
    LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0, 0, 0);
    int granularity = 60000;
    int machineId = 4249;
    int kpiID = 9;

    @Test
    public void getKpiLclUcl() throws IOException {
        JsonNode kpiNode = GetKpiRequest.getKpiNode(getRequest(), definitionId, equipmentId);
        String lclUclType = kpiNode.path("lclUclType").asText(null);
        System.out.println("lclUclType: " + lclUclType);
        kpiData = GetKpiData.getKpiDataValue(machineId, kpiID, startTime, endTime, granularity);
        Assert.assertNotNull(kpiData, "KPI data is null");
        Assert.assertFalse(kpiData.isEmpty(), "No KPI data available");
        double mean = Stats.calculateMean(kpiData);
        double stdDev = Stats.calculateStdDev(kpiData);

        System.out.println("Mean: " + mean);
        System.out.println("Std Dev: " + stdDev);

        LclUclResult result = LclUclUtil.resolveLclUcl(lclUclType, kpiNode, mean, stdDev, "KPI");

        boolean comparison = compareStatisticsFromAPIToCalculation(getStatisticsDataOfKPIS(machineId, kpiID, startTime, endTime, granularity), Double.parseDouble(result.getLcl()), mean, stdDev, Double.parseDouble(result.getUcl()));
        if (comparison) {
            System.out.println("API Value is Matching From Calculation Value");
        } else {
            System.out.println("API Value is not Matching From Calculation Value");
        }
    }
}


