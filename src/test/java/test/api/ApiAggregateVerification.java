package test.api;

import base.api.APIBase;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.api.GetKpiData;
import page.api.GetRawParameterData;
import utils.KPISCalculationUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class ApiAggregateVerification extends APIBase {

    LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0,0,0);
    LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0,0,0);
    int kpiGranularity=60000;
    int machineID=4249;
    int kpiID=9;
    int rawParamId=45;
    int rawParamGranularity=0;
    //@Test
    public void validateSumKpi() throws IOException {
        Map<String, String> kpiData = GetKpiData.getKpiDataValue(machineID, kpiID, startTime,endTime,kpiGranularity);
        Map<String, String> rawParameterData = GetRawParameterData.getRawParameterDataValue(machineID, rawParamId, startTime,endTime,rawParamGranularity);
        Map<String, KPISCalculationUtils.VerificationResult> pureCalculationMap = KPISCalculationUtils
                .verifyAggregatedSumData(rawParameterData, kpiData);
        for (Map.Entry<String, KPISCalculationUtils.VerificationResult> entry : pureCalculationMap.entrySet()) {
            String aggTime = entry.getKey();
            KPISCalculationUtils.VerificationResult result = entry.getValue();
            int expected = (int) result.expected;
            int actual = (int) result.actual;
            System.out.println("Time: " + aggTime + " | Expected(Sum from raw): " + result.expected
                    + " | Actual(Aggregated API): " + result.actual
                    + " | Match: " + result.isValid);
            Assert.assertEquals(actual, expected, "Mismatch at " + aggTime + " Expected=" + expected + " Actual=" + actual);
        }
    }

    @Test
    public void validateSubKpi() throws IOException {
        Map<String, String> kpiData = GetKpiData.getKpiDataValue(machineID,kpiID,startTime,endTime,kpiGranularity);
        Map<String, String> rawParameterData = GetRawParameterData.getRawParameterDataValue(machineID, rawParamId, startTime,endTime,rawParamGranularity);
        Map<String, KPISCalculationUtils.VerificationResult> cummulativeCalculationMap = KPISCalculationUtils
                .verifyAggregatedData(rawParameterData, kpiData);
        for (Map.Entry<String, KPISCalculationUtils.VerificationResult> entry : cummulativeCalculationMap.entrySet()) {
            String aggTime = entry.getKey();
            KPISCalculationUtils.VerificationResult result = entry.getValue();
            int expected = (int) result.expected;
            int actual = (int) result.actual;
            System.out.println("Time: " + aggTime + " | Expected(Sum from raw): " + result.expected
                    + " | Actual(Aggregated API): " + result.actual
                    + " | Match: " + result.isValid);
            Assert.assertEquals(actual, expected, "Mismatch at " + aggTime + " Expected=" + expected + " Actual=" + actual);

        }
    }
}