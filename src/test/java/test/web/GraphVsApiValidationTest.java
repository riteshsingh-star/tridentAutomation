package test.web;

import base.api.APIBase;
import base.web.BaseTest;
import io.qameta.allure.Allure;
import org.testng.annotations.*;
import page.web.CreateWidget;
import page.web.PageComponent;
import pojo.web.DashboardData;
import utils.CompareGraphAndApiData;
import utils.ReadJsonFile;
import org.testng.Assert;
import test.api.GetChartDataApi;
import page.web.Dashboard;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GraphVsApiValidationTest extends BaseTest {

    CreateWidget  createWidget;
    Dashboard dashboard;
    List<String> measuresName;
    private APIBase apiBase;
    PageComponent pageComponent;
    DashboardData data;

    @BeforeClass(alwaysRun = true)
    public void setupApi() {
        apiBase = new APIBase();
        apiBase.initApi();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownApi() {
        APIBase.closeApi();
    }

    @BeforeClass
    public void openWidgetPageAndCreateWidget() {
        Allure.step("Opening Widget Page");
        pageComponent =new PageComponent(page,context);
        data = ReadJsonFile.readJson("testData/dashboard.json", DashboardData.class);
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
        Assert.assertEquals(validationData,"Equipment Performance"+" "+data.equipmentName());
    }

    @Test
    public void validateGraphWithApi() throws InterruptedException, IOException {
        Map<String, String> uiList = pageComponent.getChartData(0, 2,0);
        System.out.println("uiList: " + uiList);
        Map<String, String> apiList = GetChartDataApi.getTimeSeriesDataAccordingToKPIS();
        System.out.println("apiList: " + apiList);
        Assert.assertEquals(uiList.size(), apiList.size(), "UI and API data count mismatch");
        CompareGraphAndApiData.compareStringMaps(uiList, apiList);
    }
}
