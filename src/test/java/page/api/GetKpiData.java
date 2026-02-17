package page.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import config.EnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.api.DateRange;
import pojo.api.EquipKpi;
import pojo.api.EquipKpiRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static base.api.APIService.postApiRequest;
import static utils.ApiHelper.fetchApiData;
import static utils.ParseTheTimeFormat.convertToUtc;

public class GetKpiData extends APIBase {

    static String pathURL = EnvironmentConfig.getKPIPath();

    public static String getKpiDataAPI(int machineID, int kpiID, DateRange dateRange, int granularity) {
        EquipKpi equipKpi = new EquipKpi(machineID, List.of(kpiID));
        EquipKpiRequest request = new EquipKpiRequest(List.of(equipKpi), dateRange, granularity, true);
        APIResponse response = postApiRequest(request, pathURL);
        System.out.println(response.url());
        return response.text();
    }

    public static Map<String, String> getKpiDataValue(int machineID, int kpiID, LocalDateTime startTime, LocalDateTime endTime, int granularity) throws IOException {
        String startUtc = convertToUtc(startTime);
        String endUtc = convertToUtc(endTime);
        DateRange dateRange = new DateRange(startUtc, endUtc);
        String responseJson = getKpiDataAPI(machineID, kpiID, dateRange, granularity);
        Map<String, String> kpiData = fetchApiData(responseJson, "equipKpis", "kpis", false);
        return kpiData;
    }

}
