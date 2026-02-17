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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static base.api.APIService.postApiRequest;
import static utils.ApiHelper.getKpiAggregateData;
import static utils.ParseTheTimeFormat.convertToUtc;

public class GetAggregateValueAndTypeOfKpi extends APIBase {

    static String pathURL = EnvironmentConfig.getKpiAggregatePath();
    static LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);
    static LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0, 0, 0);
    static int machineID=4249;
    static int kpiID=9;

    public static String getKpiAggregateDataFromApi(int machineID, int kpiID, DateRange dateRange){
        EquipKpi kpiDetails = new EquipKpi(machineID, List.of(kpiID));
        KpiAggregateRequest request = new KpiAggregateRequest(List.of(kpiDetails), dateRange);
        APIResponse response = postApiRequest(request, pathURL);
        return response.text();
    }

    @Test
    public static void getKPIAggregateValue() throws JsonProcessingException {
        String startUtc = convertToUtc(startTime);
        String endUtc = convertToUtc(endTime);
        DateRange dateRange = new DateRange(startUtc, endUtc);
        String responseJson = getKpiAggregateDataFromApi(machineID,kpiID,dateRange);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseJson);
        Map<String, Object> dataValue = getKpiAggregateData(rootNode);
        System.out.println(dataValue);
    }
}
