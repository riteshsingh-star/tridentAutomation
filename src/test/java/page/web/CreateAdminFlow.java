package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CreateAdminFlow extends BasePage {

    Page page;
    BrowserContext context;
    Page childPage;

    public CreateAdminFlow(Page page, BrowserContext context) {
        super(page);

        this.context = context;
        this.childPage = OpenAdminPage.moveToAdminPage(page, context);
    }

    // public void createGlobalParameter() throws InterruptedException {
    //
    // childPage.click("li[id='Global Parameters List']");
    // syncUntil(2000);
    // childPage.click("//button[@id='createNewGlobalParameter']");
    // syncUntil(1000);
    // childPage.fill("//input[@id='parameterName']","NewBatchBB");
    // syncUntil(1000);
    // childPage.click("//*[@class=' css-1hwfws3']");
    // syncUntil(1000);
    // childPage.click("//*[contains(text(),'Double')]");
    // syncUntil(1000);
    // childPage.check("input[value='batch']");
    // syncUntil(1000);
    // childPage.click("//span[text()='Save']//parent::button");
    // }
    //
    public void dragNewBatchBatchToDefinition() {

        Locator source = childPage.locator("text=NewBatchBB");

        Locator target = childPage.getByRole(AriaRole.TEXTBOX);

        source.dragTo(target);
    }

    public void createNewKPIDefinition() throws InterruptedException {

        childPage.click("li[id='New KPI Definition']");
        syncUntil(2000);
        childPage.fill("//input[@id='kpiName']", "BH");
        childPage.selectOption("#selectedPlant", "covacsis_dev");
        childPage.getByRole(AriaRole.CHECKBOX,
                new Page.GetByRoleOptions().setName("Aluminium"))
                .check();
        childPage.check("input[value='batch']");
        childPage.click(
                "body > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(3) > button:nth-child(1)");
        syncUntil(2000);
        childPage.locator("(//li[@class='jss18245 active'])[1]").click();
        dragNewBatchBatchToDefinition();
        syncUntil(2000);
        childPage.selectOption(
                "body > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(6) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2) > select:nth-child(1)",
                "AVERAGE");
        syncUntil(2000);
        childPage.selectOption("(//select[@id='fromUnit'])[2]", "More Value is Good");
        syncUntil(2000);
        childPage.selectOption("(//select[@id='fromUnit'])[3]", "HOUR");
        childPage.fill("//input[@id='precision']", "2");
        childPage.click("(//button[@type='button'])[8]");
    }

    public void addLogicToTheKPIAndValidate() {

    }
}
