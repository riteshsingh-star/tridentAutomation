package test;

import page.web.CreateAdminFlow;
import page.web.LoginPage;
import base.web.BaseTest;
import org.testng.annotations.Test;

public class AdminFlowTest extends BaseTest {

    @Test
    public void createAdminFowTest() throws InterruptedException {
        CreateAdminFlow adminFlow=new CreateAdminFlow(page, context);
        //adminFlow.createGlobalParameter();
        //adminFlow.createNewKPIDefinition();
        adminFlow.addLogicToTheKPIAndValidate();

    }
}
