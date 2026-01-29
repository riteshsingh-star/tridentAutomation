package test;

import com.microsoft.playwright.options.LoadState;
import page.web.EquipmentPageDataVerification;
import page.web.PageComponent;
import pojo.DashboardData;
import utils.ReadJsonFile;
import page.web.Dashboard;
import base.web.BaseTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class DashboardTest extends BaseTest {

    @Test
    public void createDashboardTest() throws InterruptedException, IOException {
        PageComponent pageComponent =new PageComponent(page);
        DashboardData data =
                ReadJsonFile.readJson("testData/dashboard.json", DashboardData.class);
        List<String> measuresName = data.measuresName();
        Dashboard dashboard = new Dashboard(page);
        //dashboard.createDashboard(data.dashboardName, data.dashboardDescription,data.visibilityType);
        dashboard.searchDashboard(data.dashboardName());
        dashboard.openWidgetCreationPage();
        dashboard.addEquipmentTrendWidget(data.equipmentName(), measuresName, data.time(), data.granularity());
        //dashboard.createEquipmentStoppageDonutWidget(data.equipmentName);
        //dashboard.createEquipmentBatchDetailsWidget(data.equipmentName);

        //dashboard.createBatchTrendWidget(data.equipmentName, measuresName);
        //dashboard.createEquipmentWidget(data.equipmentName, data.viewType,true);
        dashboard.saveTheWidget();
        System.out.println(pageComponent.getChartData(0,2,0));
        //dashboard.deleteDashboard(data.dashboardName);
    }
}
