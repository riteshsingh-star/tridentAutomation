package test.api;

import base.api.APIBase;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.KPISCalculationUtils;
import java.io.IOException;
import java.util.Map;

public class ApiAggregateVerification extends APIBase {

   //@Test
    public static void validateSumKpi() throws IOException {
        Map<String, String> kpiData = GetKpiData.getKpiDataUsingMapPojo();
        Map<String, String> rawParameterData = GetRawParameterData.getRawParameterDataUsingPojo();
        Map<String, KPISCalculationUtils.VerificationResult> waterCalculationMap = KPISCalculationUtils
                .verifyAggregatedSumData(rawParameterData, kpiData);
        for (Map.Entry<String, KPISCalculationUtils.VerificationResult> entry : waterCalculationMap.entrySet()) {
            String aggTime = entry.getKey();
            KPISCalculationUtils.VerificationResult result = entry.getValue();
            int expected = (int) result.expected;
            int actual = (int) result.actual;
            System.out.println("Time: " + aggTime + " | Expected(Sum from raw): " + result.expected
                    + " | Actual(Aggregated API): " + result.actual
                    + " | Match: " + result.isValid);
            Assert.assertEquals(actual, expected,
                    "Mismatch at " + aggTime + " Expected=" + expected + " Actual=" + actual);
        }
    }

    @Test
    public static void validateSubKpi() throws IOException {
        Map<String, String> kpiData = GetKpiData.getKpiDataUsingMapPojo();
        Map<String, String> rawParameterData = GetRawParameterData.getRawParameterDataUsingPojo();
        Map<String, KPISCalculationUtils.VerificationResult> waterCalculationMap = KPISCalculationUtils
                .verifyAggregatedData(rawParameterData, kpiData);
        for (Map.Entry<String, KPISCalculationUtils.VerificationResult> entry : waterCalculationMap.entrySet()) {
            String aggTime = entry.getKey();
            KPISCalculationUtils.VerificationResult result = entry.getValue();
            int expected = (int) result.expected;
            int actual = (int) result.actual;
            System.out.println("Time: " + aggTime + " | Expected(Sum from raw): " + result.expected
                    + " | Actual(Aggregated API): " + result.actual
                    + " | Match: " + result.isValid);
            Assert.assertEquals(actual, expected,
                    "Mismatch at " + aggTime + " Expected=" + expected + " Actual=" + actual);

        }
    }
}