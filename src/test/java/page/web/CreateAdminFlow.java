package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CreateAdminFlow extends BasePage {


    BrowserContext context;
    Page childPage;

    public CreateAdminFlow(Page page, BrowserContext context) {
        super(page);
        this.context = context;
        this.childPage = OpenAdminPage.moveToAdminPage(page, context);
    }

    public void createGlobalParameter() throws InterruptedException {

        childPage.click("li[id='Global Parameters List']");
        syncUntil(2000);
        childPage.click("//button[@id='createNewGlobalParameter']");
        syncUntil(1000);
        childPage.fill("//input[@id='parameterName']","New_Batch");
        syncUntil(1000);
        childPage.click(".css-x1u5dn-control");
        childPage.click("text=Integer");
        syncUntil(1000);
        childPage.check("input[value='batch']");
        syncUntil(1000);
        childPage.click(".jss242");
    }

    public  void createNewKPIDefinition() throws InterruptedException{

        childPage.click("li[id='New KPI Definition']");
        syncUntil(2000);
        childPage.check(".jss134 jss128 jss568 jss572 jss576");
        childPage.getByRole(AriaRole.CHECKBOX,
                        new Page.GetByRoleOptions().setName("Aluminium"))
                .check();
        childPage.check(".jss571");
        childPage.click("(//button[@type='button'])[7]");



    }

    public  void addLogicToTheKPIAndValidate(){

    }
}

