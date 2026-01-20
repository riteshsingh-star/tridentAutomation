package test;

import page.Dashboard;
import base.BaseTest;
import org.testng.annotations.Test;
import page.LoginPage;

public class DashboardTest extends BaseTest {

    @Test
    public void createDashboardTest() throws InterruptedException {

        LoginPage login = new LoginPage(page);
        login.login("covacsis_admin@techprescient.com", "MqdYgv29wAq5nG8CZY58B");
        Dashboard dashboard = new Dashboard(page);
        dashboard.createDashboard("Test1234Auto", "TestingAutomation");
    }
}
