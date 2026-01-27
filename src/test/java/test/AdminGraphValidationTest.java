package test;

import base.api.APIBase;
import base.web.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page.api.GetChartDataApi;
import page.web.DashboardVerify;

import java.io.IOException;
import java.util.Map;


public class AdminGraphValidationTest extends BaseTest {

    private APIBase apiBase;

    @BeforeMethod(alwaysRun = true)
    public void setupApi() {
        apiBase = new APIBase();
        apiBase.initApi();
    }
    @Test
    public void validateGraphwithmyApi() throws InterruptedException, IOException {

        DashboardVerify dashboardv = new DashboardVerify(page);

        dashboardv.EquipmentPage("singeing");
        Map<String ,String> uiList = dashboardv.getChartData(0,2,0);
        System.out.println("Ui List data "+uiList);
        Map<String , String > apiList = GetChartDataApi.getTimeSeriesDataAccordingToKPIS();
        System.out.println("apiList " + apiList);
        Assert.assertEquals(uiList.size(), apiList.size(),
                "UI and API data count mismatch");


    }
}