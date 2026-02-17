package page.api;

import base.api.APIBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pojo.api.DateRange;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static page.api.GetKpiData.getKpiDataAPI;
import static utils.ApiHelper.fetchStatisticsData;
import static utils.ParseTheTimeFormat.convertToUtc;


public class GetStatisticsDataFromAPI extends APIBase {


    public static Map<String, Double> getStatisticsDataOfKPIS(int machineId, int kpiID, LocalDateTime startTime,LocalDateTime endTime, int granularity) throws IOException {
        String startUtc = convertToUtc(startTime);
        String endUtc = convertToUtc(endTime);
        DateRange dateRange = new DateRange(startUtc, endUtc);
        String responseJson = getKpiDataAPI(machineId, kpiID,dateRange, granularity);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseJson);
        Map<String, Double> stats = fetchStatisticsData(rootNode);
        System.out.println(stats);
        return stats;
    }
}
