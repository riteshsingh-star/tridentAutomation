package page.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import config.EnvironmentConfig;
import org.testng.Assert;
import pojo.api.DateRange;
import pojo.api.EquipKpi;
import pojo.api.EquipKpiRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static base.api.APIService.postApiRequest;
import static utils.ApiHelper.fetchApiData;

public class GetKpiData extends APIBase {

    static String pathURL = EnvironmentConfig.getKPIPath();

    public static String getKpiData() {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> equipKpis = new HashMap<>();
        equipKpis.put("id", 4248);
        equipKpis.put("kpiParamDefIds", List.of(9));
        payload.put("equipKpis", List.of(equipKpis));
        Map<String, Object> dateRange = new HashMap<>();
        dateRange.put("startTs", "2026-01-28T04:30:00.000Z");
        dateRange.put("endTs", "2026-01-29T04:30:00.000Z");
        payload.put("dateRange", dateRange);
        payload.put("granularityInMillis", 60000L);
        payload.put("addMissingTimestamps", true);
        APIResponse response = postApiRequest(payload, pathURL);
        Assert.assertEquals(response.status(), 200);
        return response.text();
    }

    public static Map<String, String> getKpiDataUsingMap() throws IOException {
        String responseJson = getKpiData();
        Map<String, String> apiValues = fetchApiData(responseJson, "equipKpis", "kpis", false);
        return apiValues;
    }

    public static String getKpiDataPojo() {
        EquipKpi equipKpi = new EquipKpi(4249, List.of(9));
        DateRange dateRange = new DateRange("2026-01-26T04:30:00.000Z", "2026-01-27T04:30:00.000Z");
        EquipKpiRequest request = new EquipKpiRequest(List.of(equipKpi), dateRange, 60000, true);
        APIResponse response = postApiRequest(request, pathURL);
        System.out.println(response.url());
        Assert.assertEquals(response.status(), 200);
        return response.text();
    }

    public static Map<String, String> getKpiDataUsingMapPojo() throws IOException {
        String responseJson = getKpiDataPojo();
        return fetchApiData(responseJson, "equipKpis", "kpis", false);
    }

}
