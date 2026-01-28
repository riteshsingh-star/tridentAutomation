package test;

import base.api.APIBase;
import base.web.BaseTest;
import page.web.PageComponent;
import pojo.DashboardData;
import utils.CompareGraphAndApiData;
import utils.ReadJsonFile;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page.api.GetChartDataApi;
import page.web.Dashboard;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GraphVsApiValidationTest extends BaseTest {

    private APIBase apiBase;
    PageComponent pageComponent;
    @BeforeMethod(alwaysRun = true)
    public void setupApi() {
        apiBase = new APIBase();
        apiBase.initApi();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownApi() {
        APIBase.closeApi();
    }

    @Test
    public void validateGraphWithApi() throws InterruptedException, IOException {
        pageComponent =new PageComponent(page);
        DashboardData data =
                ReadJsonFile.readJson("testdata/dashboard.json", DashboardData.class);
        List<String> measuresName = data.measuresName();
        Dashboard dashboard = new Dashboard(page);
        //dashboard.createDashboard(data.dashboardName, data.dashboardDescription,"Public");
        dashboard.searchDashboard(data.dashboardName());
        dashboard.openWidgetCreationPage();
        dashboard.addEquipmentTrendWidget(data.equipmentName(), measuresName, data.time(), data.granularity());
        dashboard.saveTheWidget();
        Map<String, String> uiList = pageComponent.getChartData(0, 2,1);
        System.out.println("uiList: " + uiList);
        Map<String, String> apiList = GetChartDataApi.getTimeSeriesDataAccordingToKPIS();
        System.out.println("apiList: " + apiList);
        Assert.assertEquals(uiList.size(), apiList.size(), "UI and API data count mismatch");
        CompareGraphAndApiData.compareStringMaps(uiList, apiList);
    }
}
