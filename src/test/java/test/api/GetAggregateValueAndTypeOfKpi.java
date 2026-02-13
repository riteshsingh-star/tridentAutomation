package test.api;

import base.api.APIBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import config.EnvironmentConfig;
import org.testng.Assert;
import pojo.api.DateRange;
import pojo.api.EquipKpi;
import pojo.api.KpiAggregateRequest;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;

import static base.api.APIService.postApiRequest;
import static utils.ApiHelper.getKpiAggregateData;

public class GetAggregateValueAndTypeOfKpi extends APIBase {

    static String pathURL = EnvironmentConfig.getKpiAggregatePath();

    public static String getKpiAggregateDataFromApi() {
        EquipKpi kpiDetails = new EquipKpi(661, List.of(23));
        DateRange dateRange = new DateRange("2026-01-28T04:30:00.000Z", "2026-01-29T04:30:00.000Z");
        KpiAggregateRequest request = new KpiAggregateRequest(List.of(kpiDetails), dateRange);
        APIResponse response = postApiRequest(request, pathURL);
        Assert.assertEquals(response.status(), 200);
        return response.text();
    }

    @Test
    public static void getKPIAggregateValue() throws JsonProcessingException {
        String responseJson = getKpiAggregateDataFromApi();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseJson);
        Map<String, Object> dataValue = getKpiAggregateData(rootNode);
        System.out.println(dataValue);
    }
}
