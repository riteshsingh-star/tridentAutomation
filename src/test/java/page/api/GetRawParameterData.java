package page.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import config.EnvironmentConfig;
import pojo.api.*;
import org.testng.Assert;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static base.api.APIService.postApiRequest;
import static utils.ApiHelper.fetchApiData;
import static utils.ParseTheTimeFormat.convertToUtc;

public class GetRawParameterData extends APIBase {

    static String pathURL = EnvironmentConfig.getRawParameterPath();

    public static String getRawParaDataApi(int machineId, int rawParameterId, DateRange dateRange, int granularity) {
        Raws rawData = new Raws(machineId, List.of(rawParameterId));
        RawRequest request = new RawRequest(List.of(rawData), dateRange, granularity, true);
        APIResponse response = postApiRequest(request, pathURL);
        return response.text();
    }

    public static Map<String, String> getRawParameterDataValue(int machineId, int rawParamID, LocalDateTime startTime, LocalDateTime endTime, int granularity) throws IOException {
        String startUtc = convertToUtc(startTime);
        String endUtc = convertToUtc(endTime);
        DateRange dateRange = new DateRange(startUtc, endUtc);
        String responseJson = getRawParaDataApi(machineId, rawParamID,dateRange,granularity);
        Map<String, String> apiValues = fetchApiData(responseJson, "equipments", "rawParameters", false);
        return apiValues;
    }
}