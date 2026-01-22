package test;

import page.web.Dashboard;
import base.web.BaseTest;
import org.testng.annotations.Test;
import page.web.LoginPage;

public class DashboardTest extends BaseTest {

    @Test
    public void createDashboardTest() throws InterruptedException {

        LoginPage login = new LoginPage(page);
        login.login("covacsis_admin@techprescient.com", "MqdYgv29wAq5nG8CZY58B");
        Dashboard dashboard = new Dashboard(page);
        //dashboard.createDashboard("Test1234Auto", "TestingAutomation");
        dashboard.searchDashboard("Test1234Auto");
        //dashboard.openWidgetCreationPage();
        //dashboard.addEquipmentTrendWidget("Equipment Trend", "SINGEING", List.of("MachineDuration","Availability"),"This Week", "Two Hour");
        //dashboard.createEquipmentStoppageDonutWidget("Equipment Stoppage Donut", "SINGEING");
        //dashboard.createEquipmentBatchDetailsWidget("Equipment Batch Details", "SINGEING");

        //dashboard.createBatchTrendWidget("Batch Trend", "SINGEING", List.of("MachineDuration","Availability"));
        //dashboard.createEquipmentWidget("Equipment", "SINGEING", "Expanded",true);
        dashboard.readToolTipData();
    }
}
