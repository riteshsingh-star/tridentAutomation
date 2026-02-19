package test.web;

import base.api.APIBase;
import base.web.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page.api.GetStatisticsDataFromAPI;
import page.web.DashboardVerify;
import page.web.PageComponent;
import static utils.AssertionUtil.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static factory.ApiFactory.initApi;
import static page.api.GetKpiData.getKpiDataValue;


public class AdminGraphValidationTest extends BaseTest {

    private APIBase apiBase;
    PageComponent pageComponent;

    LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0,0,0);
    LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0,0,0);
    int granularity=60000;
    int machineID=661;
    int kpiID=23;

    @BeforeMethod(alwaysRun = true)
    public void setupApi() {
        apiBase = new APIBase();
        apiBase.setUpAPI();
    }


    @Test
    public void validateGraphWithMyApi() throws InterruptedException, IOException {
        pageComponent =new PageComponent(page,context);
        DashboardVerify dashboardV = new DashboardVerify(page,context);
        dashboardV.EquipmentPage("singeing");
        Map<String ,String> uiList = pageComponent.getChartData(0);
        System.out.println("Ui List data "+uiList);
        Map<String , String > apiList = getKpiDataValue(machineID,kpiID,startTime,endTime,granularity);
        System.out.println("apiList " + apiList);
        softAssertEquals(uiList.size(), apiList.size(), "UI and API data count mismatch");



    }
}