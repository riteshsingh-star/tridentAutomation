package test;


import base.web.BaseTest;
import org.testng.annotations.Test;

import page.web.DashboardVerify;
import page.web.LoginPage;

public class DashBoardVerifyTest extends BaseTest {

    @Test
    public void Dashboardverify() throws InterruptedException {
        LoginPage login = new LoginPage(page);
        login.login("covacsis_admin@techprescient.com", "MqdYgv29wAq5nG8CZY58B");
        DashboardVerify dashboardv=new DashboardVerify(page , context);
        dashboardv.EquipmentPage(10,00,"AM");


    }




}
