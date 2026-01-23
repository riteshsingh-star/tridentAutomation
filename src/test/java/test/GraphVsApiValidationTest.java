package test;

import base.api.APIBase;
import base.web.BaseTest;
import com.trident.playwright.pojo.DashboardData;
import com.trident.playwright.utils.ReadJsonFile;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page.api.GetCharDataApi;
import page.web.Dashboard;
import page.web.LoginPage;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class GraphVsApiValidationTest extends BaseTest {

    private APIBase apiBase;

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
        DashboardData data =
                ReadJsonFile.readJson("testdata/dashboard.json", DashboardData.class);
        List<String> measuresName = data.measuresName;
        Dashboard dashboard = new Dashboard(page);
        //dashboard.createDashboard(data.dashboardName, data.dashboardDescription);
        dashboard.searchDashboard(data.dashboardName);
        //dashboard.openWidgetCreationPage();
        //dashboard.addEquipmentTrendWidget(data.widgetType, data.equipmentName, measuresName,data.time, data.granularity);
        //dashboard.saveTheWidget();
        Set<String> uiList = dashboard.getChartData();
        System.out.println("uiList: " + uiList);
        Set<String> apiList = GetCharDataApi.getTimeSeriesDataAccordingToKPIS();
        System.out.println("apiList: " + apiList);
        Assert.assertEquals(uiList.size(), apiList.size(),
                "UI and API data count mismatch");

        // 2. Exact order comparison
        Assert.assertEquals(uiList, apiList,
                "UI and API graph data mismatch");
        System.out.println(apiList.containsAll(uiList));
    }
}
