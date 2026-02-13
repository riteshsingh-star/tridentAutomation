package test.web;

import base.api.APIBase;
import base.web.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page.api.GetStatisticsDataFromAPI;
import page.web.DashboardVerify;
import page.web.PageComponent;

import java.io.IOException;
import java.util.Map;

import static factory.ApiFactory.initApi;


public class AdminGraphValidationTest extends BaseTest {

    private APIBase apiBase;
    PageComponent pageComponent;

    @BeforeMethod(alwaysRun = true)
    public void setupApi() {
        apiBase = new APIBase();
        apiBase.setUpAPI();
    }


    @Test
    public void validateGraphWithMyApi() throws InterruptedException, IOException {
        pageComponent =new PageComponent(page,context);
        DashboardVerify dashboardV = new DashboardVerify(page,context);
        dashboardV.EquipmentPage("singeing");
        Map<String ,String> uiList = pageComponent.getChartData(0,2,0);
        System.out.println("Ui List data "+uiList);
        Map<String , String > apiList = GetStatisticsDataFromAPI.getTimeSeriesDataAccordingToKPIS();
        System.out.println("apiList " + apiList);
        Assert.assertEquals(uiList.size(), apiList.size(), "UI and API data count mismatch");


    }
}