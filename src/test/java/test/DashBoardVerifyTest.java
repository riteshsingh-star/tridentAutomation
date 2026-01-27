package test;


import base.web.BaseTest;
import org.testng.annotations.Test;

import page.web.DashboardVerify;
import page.web.LoginPage;

import java.io.IOException;

public class DashBoardVerifyTest extends BaseTest {

    @Test
    public void Dashboardverify() throws InterruptedException, IOException {
        DashboardVerify dashboardv=new DashboardVerify(page);
        dashboardv.EquipmentPage("singeing");
        //dashboardv.verifyChartData();


    }




}
