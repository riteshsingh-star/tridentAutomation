package test.web;

import base.api.APIBase;
import base.web.BaseTest;
import io.qameta.allure.Allure;
import org.testng.annotations.*;
import page.web.CreateWidget;
import page.web.PageComponent;
import pojo.web.DashboardData;
import page.api.GetKpiData;
import utils.CompareGraphAndApiData;
import utils.ReadDataFile;
import page.web.Dashboard;
import base.api.APIBase.*;
import static utils.AssertionUtil.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;



public class GraphVsApiValidationTest extends BaseTest {

    APIBase baseAPI;
    CreateWidget  createWidget;
    Dashboard dashboard;
    List<String> measuresName;
    PageComponent pageComponent;
    DashboardData data;
    LocalDateTime startTime = LocalDateTime.of(2026, 2, 9, 10, 0,0,0);
    LocalDateTime endTime = LocalDateTime.of(2026, 2, 16, 10, 0,0,0);
    int granularity=28800000;

    @BeforeClass(alwaysRun = true)
    public void setupApi() {
        baseAPI = new APIBase();
        baseAPI.setUpAPI();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownApi() {
        baseAPI.tearDownAPI();
    }

    @BeforeClass
    public void openWidgetPageAndCreateWidget() throws Exception {
        Allure.step("Opening Widget Page");
        pageComponent =new PageComponent(page,context);
        data = ReadDataFile.loadDataFile(DashboardData.class);
        measuresName = data.measuresName();
        dashboard = new Dashboard(page,context);
        createWidget = new CreateWidget(page, context);
        //dashboard.createDashboard(data.dashboardName, data.dashboardDescription,"Public");
        dashboard.searchDashboard(data.dashboardName());
        createWidget.openWidgetCreationPage();
    }

    @Test
    public void createAndValidateWidget() {
        Allure.step("Creating Widget");
        String validationData= createWidget.addEquipmentTrendWidget(data.equipmentName(), measuresName, data.time(), data.granularity());
        softAssertEquals(validationData,"Equipment Performance"+" "+data.equipmentName(), "Widget name should match expected value");
        assertAll();
    }

    @Test
    public void validateGraphWithApi() throws InterruptedException, IOException {
        Map<String, String> uiData = pageComponent.getChartData(0);
        System.out.println("uiList: " + uiData);
        Map<String, String> apiData = GetKpiData.getKpiDataValue(661,23,startTime,endTime,granularity);
        System.out.println("apiList: " + apiData);
        softAssertEquals(uiData.size(), apiData.size(), "UI and API data count mismatch");
        CompareGraphAndApiData.compareStringMaps(uiData, apiData);
        assertAll();
    }
}
