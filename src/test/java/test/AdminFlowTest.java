package test;

import page.web.CreateAdminFlow;
import page.web.LoginPage;
import base.web.BaseTest;
import org.testng.annotations.Test;

public class AdminFlowTest extends BaseTest {

    @Test
    public void createAdminFowTest() throws InterruptedException {
        LoginPage login = new LoginPage(page);
        login.login("covacsis_admin@techprescient.com", "MqdYgv29wAq5nG8CZY58B");
        CreateAdminFlow adminFlow=new CreateAdminFlow(page, context);
        adminFlow.createGlobalParameter("NEWBBW");
        //adminFlow.createNewKPIDefinition("TESTQAA");
        //adminFlow.addLogicToTheKPIAndValidate();

    }
}
