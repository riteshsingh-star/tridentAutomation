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
import utils.ReadJsonFile;
import org.testng.Assert;
import page.web.Dashboard;
import base.api.APIBase.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;



public class GraphVsApiValidationTest extends BaseTest {

    APIBase baseAPI;
    CreateWidget  createWidget;
    Dashboard dashboard;
    List<String> measuresName;
    PageComponent pageComponent;
    DashboardData data;

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
    public void openWidgetPageAndCreateWidget() {
        Allure.step("Opening Widget Page");
        pageComponent =new PageComponent(page,context);
        data = ReadJsonFile.readJson("dashboard.json", DashboardData.class);
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
        Map<String, String> uiData = pageComponent.getChartData(0, 2,0);
        System.out.println("uiList: " + uiData);
        Map<String, String> apiData = GetKpiData.getKpiDataUsingMapPojo();
        System.out.println("apiList: " + apiData);
        Assert.assertEquals(uiData.size(), apiData.size(), "UI and API data count mismatch");
        CompareGraphAndApiData.compareStringMaps(uiData, apiData);
    }
}
