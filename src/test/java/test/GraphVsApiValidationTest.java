package test;

import base.api.APIBase;
import base.web.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page.api.GetCharDataApi;
import page.web.Dashboard;
import page.web.LoginPage;

import java.io.IOException;
import java.util.List;

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
        LoginPage login = new LoginPage(page);
        login.login("covacsis_admin@techprescient.com", "MqdYgv29wAq5nG8CZY58B");

        Dashboard dashboard = new Dashboard(page);
        dashboard.createDashboard("Test1234Auto", "TestingAutomation");
        dashboard.searchDashboard("Test1234Auto");
        dashboard.openWidgetCreationPage();
        dashboard.addEquipmentTrendWidget("Equipment Trend", "SINGEING", List.of("MachineDuration"),"This Week", "Two Hour");
        List<String> uiList = dashboard.getChartData();

        GetCharDataApi api = new GetCharDataApi();
        List<String> apiList = GetCharDataApi.getTimeSeriesDataAccordingToKPIS();
        Assert.assertEquals(uiList.size(), apiList.size(),
                "UI and API data count mismatch");

        // 2. Exact order comparison
        /*Assert.assertEquals(uiList, apiList,
                "UI and API graph data mismatch");*/
        System.out.println(apiList.containsAll(uiList));
    }
}
