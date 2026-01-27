package test;

import base.api.APIBase;
import base.web.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import page.web.DashboardVerify;

import java.io.IOException;
import java.util.Set;

public class AdminGraphValidationTest extends BaseTest {

    private APIBase apiBase;

    @BeforeMethod(alwaysRun = true)
    public void setupApi() {
        apiBase = new APIBase();
        apiBase.initApi();
    }

    public void validateGraphwithmyApi() throws InterruptedException, IOException {

        DashboardVerify dashboardv = new DashboardVerify(page);

        dashboardv.EquipmentPage();
        Set<String> uiList = dashboardv.getChartData();
        Set<String> apiList = VerifyKPIImplementation.getTimeSeriesDataAccordingToKPIS();
        VerifyKPIImplementation api = new VerifyKPIImplementation();
        Assert.assertEquals(uiList.size(), apiList.size(),
                "UI and API data count mismatch");
        System.out.println(apiList.containsAll(uiList));

    }
}