package page.api;

import base.api.APIBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import static page.api.GetKpiData.getKpiDataPojo;
import static utils.ApiHelper.fetchApiData;
import static utils.ApiHelper.fetchStatisticsData;


public class GetStatisticsDataFromAPI extends APIBase {

    public static Map<String, String> getTimeSeriesDataAccordingToKPIS() throws IOException {
        String responseJson = getKpiDataPojo();
        Map<String, String> apiValues = fetchApiData(responseJson, "equipKpis", "kpis", false);
        //System.out.println("API Response after Formatting: " + apiValues);
        System.out.println(apiValues.size());
        return apiValues;
    }

    public static Map<String, Double> getStatisticsDataOfKPIS() throws IOException {
        String responseJson = getKpiDataPojo();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseJson);
        Map<String, Double> stats=fetchStatisticsData(rootNode);
        System.out.println(stats);
        return stats;
    }
}
