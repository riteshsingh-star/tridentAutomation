package page;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public class CreateAdminFlow {

    Page page;
    BrowserContext context;
    Page childPage;

    public CreateAdminFlow(Page page, BrowserContext context) {
        this.page = page;
        this.context = context;
        this.childPage = OpenAdminPage.moveToAdminPage(page, context);
    }

    public void createGlobalParameter() {
        childPage.click("createGlobalParameter");
    }

    public  void createNewKPIDefinition() {
        childPage.click("createNewKPIDefinition");
    }

    public  void addLogicToTheKPIAndValidate(){

    }
}

