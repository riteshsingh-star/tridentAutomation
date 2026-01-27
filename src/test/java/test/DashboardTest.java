package test;

import com.microsoft.playwright.options.LoadState;
import com.trident.playwright.pojo.DashboardData;
import com.trident.playwright.utils.ReadJsonFile;
import page.web.Dashboard;
import base.web.BaseTest;
import org.testng.annotations.Test;
import page.web.EquipmentPageDataVerification;

import java.io.IOException;
import java.util.List;

public class DashboardTest extends BaseTest {

    @Test
    public void createDashboardTest() throws InterruptedException, IOException {

        DashboardData data =
                ReadJsonFile.readJson("testdata/dashboard.json", DashboardData.class);
        //List<String> measuresName = data.measuresName;
        //Dashboard dashboard = new Dashboard(page);
        //dashboard.createDashboard(data.dashboardName, data.dashboardDescription,"Public");
        //dashboard.searchDashboard(data.dashboardName);
        //dashboard.openWidgetCreationPage();
        //dashboard.addEquipmentTrendWidget(data.widgetType, data.equipmentName, measuresName,data.time, data.granularity);
        //dashboard.createEquipmentStoppageDonutWidget(data.widgetType, data.equipmentName);
        //dashboard.createEquipmentBatchDetailsWidget(data.widgetType, data.equipmentName);

        //dashboard.createBatchTrendWidget(data.widgetType, data.equipmentName, measuresName);
        //dashboard.createEquipmentWidget(data.widgetType, data.equipmentName, data.viewType,true);
        //dashboard.saveTheWidget();
        //System.out.println(dashboard.getChartDataLocally(0,2));
        //dashboard.deleteDashboard(data.dashboardName);

        EquipmentPageDataVerification equipmentPageVerification = new EquipmentPageDataVerification(page);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        //equipmentPageVerification.openEquipmentAndAddKPI("SINGEING","Availability","Last week");
        equipmentPageVerification.verifyTheAggregateAccordingToFormula();
    }
}
