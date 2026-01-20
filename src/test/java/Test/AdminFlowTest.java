package Test;

import Page.CreateAdminFlow;
import Page.LoginPage;
import com.trident.playwright.Base.BaseTest;
import org.testng.annotations.Test;

public class AdminFlowTest extends BaseTest {

    @Test
    public void createAdminFowTest() throws InterruptedException {
        LoginPage login = new LoginPage(page);
        login.login("covacsis_admin@techprescient.com", "MqdYgv29wAq5nG8CZY58B");
        CreateAdminFlow adminFlow=new CreateAdminFlow(page, context);
        adminFlow.createGlobalParameter();
        adminFlow.createNewKPIDefinition();


    }
}
