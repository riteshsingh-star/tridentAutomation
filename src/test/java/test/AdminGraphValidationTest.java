package test;

import base.api.APIBase;
import base.web.BaseTest;
import org.testng.annotations.BeforeMethod;

public class AdminGraphValidationTest extends BaseTest {

    private APIBase apiBase;

    @BeforeMethod(alwaysRun = true)
    public void setupApi() {
        apiBase = new APIBase();
        apiBase.initApi();
    }

/*    public void validateGraphwithmyApi() throws InterruptedException, IOException {
        LoginPage login = new LoginPage(page);
        login.login("covacsis_admin@techprescient.com", "MqdYgv29wAq5nG8CZY58B");

        DashboardVerify dashboardv = new DashboardVerify(page);

        dashboardv.EquipmentPage();
        Set<String> uiList = dashboardv.getChartData();
        Set<String> apiList = VerifyKPIImplementation.getTimeSeriesDataAccordingToKPIS();
        VerifyKPIImplementation api = new VerifyKPIImplementation();
        Assert.assertEquals(uiList.size(), apiList.size(),
                "UI and API data count mismatch");
        System.out.println(apiList.containsAll(uiList));*

    }*/
}