package page;

import base.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import page.web.OpenAdminPage;


public class CreateAdminFlow extends BasePage {


    BrowserContext context;
    Page childPage;

    public CreateAdminFlow(Page page, BrowserContext context) {
        super(page, context);

        this.context = context;
        this.childPage = OpenAdminPage.moveToAdminPage(page, context);
    }

//    public void createGlobalParameter() throws InterruptedException {
//
//        childPage.click("li[id='Global Parameters List']");
//        syncUntil(2000);
//        childPage.click("//button[@id='createNewGlobalParameter']");
//        syncUntil(1000);
//        childPage.fill("//input[@id='parameterName']","NewBatchBB");
//        syncUntil(1000);
//        childPage.click("//*[@class=' css-1hwfws3']");
//        syncUntil(1000);
//        childPage.click("//*[contains(text(),'Double')]");
//        syncUntil(1000);
//        childPage.check("input[value='batch']");
//        syncUntil(1000);
//        childPage.click("//span[text()='Save']//parent::button");
//    }
//
//    public void dragNewBatchBatchToDefinition() {
//
//        Locator source = childPage.locator("p:has-text('NEWBatchBatch')");
//        Locator target = childPage.locator(
//                "textarea[placeholder='Drag and Drop fields to define the KPI...']"
//        );
//
//        source.scrollIntoViewIfNeeded();
//        target.scrollIntoViewIfNeeded();
//
//        source.dragTo(target);
//    }
//
//    public  void createNewKPIDefinition() throws InterruptedException{
//
//        childPage.click("li[id='New KPI Definition']");
//        syncUntil(2000);
//        childPage.fill("//input[@id='kpiName']","TEstIng");
//        childPage.selectOption("#selectedPlant","covacsis_dev");
//        childPage.getByRole(AriaRole.CHECKBOX,
//                        new Page.GetByRoleOptions().setName("Aluminium"))
//                .check();
//        childPage.check("input[value='batch']");
//        childPage.click("body > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(3) > button:nth-child(1)");
//        syncUntil(2000);
//        childPage.locator("li:has(span:text-is('Global Parameters'))")
//                .click();
//        dragNewBatchBatchToDefinition();
//        syncUntil(10000);
//        childPage.selectOption("body > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(6) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2) > select:nth-child(1)","AVERAGE");
//        syncUntil(2000);
//        childPage.selectOption("(//select[@id='fromUnit'])[2]","More Value is Good");
//        syncUntil(2000);
//        childPage.selectOption("(//select[@id='fromUnit'])[3]","meter");
//        childPage.fill("//input[@id='precision']","2");
//        childPage.click("(//button[@type='button'])[8]");
//   }
//


//    public  void addLogicToTheKPIAndValidate() throws InterruptedException{
//        childPage.click("li[id='Advanced Implementation']");
//        syncUntil(2000);
//        childPage.click("(//*[name()='svg'][@role='presentation'])[32]");
//        childPage.fill("input[aria-label='Search']","NewBatchBB");
//        syncUntil(2000);
//        childPage.click("(//*[name()='svg'][@id='expandable-button'])[2]");
//        syncUntil(5000);
//        Locator definition = childPage.locator("body > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > table:nth-child(1) > tbody:nth-child(3) > tr:nth-child(2) > td:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(8) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2) > div:nth-child(1)");
//        definition.click();
//        definition.type(
//                "avg(^SINGEING.SINGED_METER)",
//                new Locator.TypeOptions().setDelay(30)
//        );
//        childPage.check("(//input[@type='checkbox'])[8]");
//        syncUntil(10000);
//
//        childPage.selectOption("//div[8]//div[1]//div[1]//div[2]//div[1]//select[1]","meter");
//        childPage.locator("button").filter(new Locator.FilterOptions().setHasText("VALIDATE")).first().click();
//
//
//        childPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).nth(1).click();
//
//        childPage.fill("input[aria-label='Search']","NewBatchB");
//        childPage.locator("tr[id='MUIDataTableBodyRow-210'] td:nth-child(3)").click();
//        syncUntil(5000);
//
//        childPage.selectOption("//div[@class='machine-661']//select[@class='multipleSelDD searchSelect form-control select-batch-frequency']","Batch Start");
//        //childPage.selectOption("//div[@class='machine-661']//select[@class='multipleSelDD searchSelect form-control select-batch-frequency']","Batch Start");
//
//        childPage.selectOption("div[class='machine-661'] select[class='multipleSelDD searchSelect form-control select-unit']","meter (N.A.)");
//
//        childPage.locator("input[value='2022-05-20']");
//
//        childPage.check("div[class='machine-661'] input[name='active']");
//
//        childPage.locator("div.btn-group.pull-right").locator("button").nth(1);
//
//    }

    public void addLogicToTheKPIAndValidate() {

        // Click "Advanced Implementation" menu
        childPage.getByText("Advanced Implementation", new Page.GetByTextOptions().setExact(true))
                .click();


        // Click search icon (better: wrap with aria-label or title if available)
        childPage.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Search")).first()
                .click();


        // Search KPI
        childPage.getByRole(AriaRole.TEXTBOX,
                        new Page.GetByRoleOptions().setName("Search"))
                .fill("NewBatchBB");


        // Expand KPI row (assuming expandable button has aria-label)
        childPage.locator("button:has(svg#expandable-button)").nth(1).click();


        // Click Definition field (best alternative using visible text or container)
        Locator row = childPage.locator("tbody tr").nth(1);

        Locator definition = row.locator("div[tabindex], [role='textbox'], textarea").first();

        definition.click();
        definition.type(
                "avg(^SINGEING.SINGED_METER)",
                new Locator.TypeOptions().setDelay(30)
        );


        // Checkbox (8th one replaced with role-based selection)
        childPage.getByRole(AriaRole.CHECKBOX).nth(7).check();

        // Select Unit dropdown
        childPage.selectOption("//div[8]//div[1]//div[1]//div[2]//div[1]//select[1]","meter");


        // Click VALIDATE button
        childPage.locator("button").filter(new Locator.FilterOptions().setHasText("VALIDATE")).first().click();

        // Click Search button
        childPage.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Search"))
                .nth(1)
                .click();

        // Search Batch
        childPage.getByLabel("Search").fill("NewBatchB");

        // Click table row using text instead of hard-coded row id
        childPage.getByRole(AriaRole.ROW)
                .filter(new Locator.FilterOptions().setHasText("NewBatchB"))
                .click();

        // Select Batch Frequency
        childPage.getByRole(AriaRole.COMBOBOX)
                .filter(new Locator.FilterOptions().setHasText("Batch Frequency"))
                .selectOption("Batch Start");

        // Select Unit
        childPage.getByRole(AriaRole.COMBOBOX)
                .filter(new Locator.FilterOptions().setHasText("Unit"))
                .selectOption("meter (N.A.)");

        // Date input (aria-label or placeholder preferred)
        childPage.getByLabel("Date").fill("2022-05-20");

        // Active checkbox
        childPage.getByRole(AriaRole.CHECKBOX,
                        new Page.GetByRoleOptions().setName("Active"))
                .check();

        // Save / Update button (second button in group)
        childPage.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Save"))
                .click();
    }

}

