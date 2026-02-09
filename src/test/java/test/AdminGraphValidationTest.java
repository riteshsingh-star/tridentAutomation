package test;

import base.api.APIBase;
import base.web.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.api.GetChartDataApi;
import page.web.DashboardVerify;
import page.web.PageComponent;

import java.io.IOException;
import java.util.Map;


public class AdminGraphValidationTest extends BaseTest {

    private APIBase apiBase;
    PageComponent pageComponent;

    @BeforeMethod(alwaysRun = true)
    public void setupApi() {
        apiBase = new APIBase();
        apiBase.initApi();
    }


    @Test
    public void validateGraphWithMyApi() throws InterruptedException, IOException {
        pageComponent =new PageComponent(page);
        DashboardVerify dashboardV = new DashboardVerify(page);
        dashboardV.EquipmentPage("singeing");
        Map<String ,String> uiList = pageComponent.getChartData(0,2,0);
        System.out.println("Ui List data "+uiList);
        Map<String , String > apiList = GetChartDataApi.getTimeSeriesDataAccordingToKPIS();
        System.out.println("apiList " + apiList);
        Assert.assertEquals(uiList.size(), apiList.size(), "UI and API data count mismatch");


    }
}