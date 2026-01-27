package page.web;
import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;


public class CreateAdminFlow extends BasePage {


    BrowserContext context;
    Page childPage;

    public CreateAdminFlow(Page page, BrowserContext context) {
        super(page);

        this.context = context;
        this.childPage = OpenAdminPage.moveToAdminPage(page, context);
    }

    public void createGlobalParameter(String name) throws InterruptedException {


        clickGlobalParameters();

        clickCreateNewParameter(name);

        clickSaveButton();


    }
        private void clickGlobalParameters() {
            childPage.getByText("Global Parameters List").click();
        }

        private void clickCreateNewParameter(String name) {

            childPage.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("CREATE NEW"))
                    .click();

            childPage.getByPlaceholder("Global Parameter Name").fill(name);


            childPage.click("//div[@class=' css-tlfecz-indicatorContainer']");

            childPage.locator("div[class*='menu']")
                    .getByText("Double", new Locator.GetByTextOptions().setExact(true))
                    .click();


            childPage.getByLabel("Batch", new Page.GetByLabelOptions()
                            .setExact(true))
                    .check();

        }

        private void clickSaveButton() {
            childPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                            .setName("Save"))
                    .click();
        }


    public void dragNewBatchBatchToDefinition() {

        Locator source = childPage.locator("p:has-text('NEWBatchBatch')");
        Locator target = childPage.locator(
                "textarea[placeholder='Drag and Drop fields to define the KPI...']"
        );

        source.scrollIntoViewIfNeeded();
        target.scrollIntoViewIfNeeded();

        source.dragTo(target);
    }

    public  void createNewKPIDefinition(String name) throws InterruptedException {

        childPage.getByText("New KPI Definition", new Page.GetByTextOptions().setExact(true))
                .click();

        defineKpi(name);

        selectVeriableandDefine();

    }

        private void defineKpi(String name) {
            childPage.getByPlaceholder("KPI Name").fill(name);

        childPage.selectOption("#selectedPlant", "covacsis_dev");

        childPage.getByRole(AriaRole.CHECKBOX,
                        new Page.GetByRoleOptions().setName("Aluminium"))
                .check();
        childPage.getByRole(AriaRole.CHECKBOX,
                       new Page.GetByRoleOptions().setName("Batch"))
                .check();
        childPage.getByRole(AriaRole.BUTTON,
                       new Page.GetByRoleOptions().setName("Continue"))
                .click();
      }

    private void selectVeriableandDefine() throws InterruptedException {


        childPage.getByText("Global Parameters",
                        new Page.GetByTextOptions().setExact(true))
                .click();
        dragNewBatchBatchToDefinition();
        syncUntil(5000);
        childPage.selectOption("#fromUnit",
                new SelectOption().setValue("SUM"));
        syncUntil(1000);
        childPage.selectOption("(//select[@id='fromUnit'])[2]","More Value is Good");

        childPage.selectOption("(//select[@id='fromUnit'])[3]", "meter");

        childPage.getByPlaceholder("Enter Precision").fill(String.valueOf(2));

        childPage.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Validate"))
                .click();
    }

    public void addLogicToTheKPIAndValidate(String name,String text,String SearchName) throws InterruptedException {

        childPage.getByText("Advanced Implementation", new Page.GetByTextOptions().setExact(true))
                .click();

        editGlobalParameterImplementations(name,text);

        editKPIImplementation(SearchName);

    }
        private void editGlobalParameterImplementations(String name , String text) {


            childPage.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Search")).first()
                    .click();


            childPage.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("Search"))
                    .fill(name);


            childPage.locator("button:has(svg#expandable-button)").nth(1).click();


            Locator equipmentHeader = childPage.locator("h4").filter(
                    new Locator.FilterOptions().setHasText(text)
            );

            equipmentHeader.scrollIntoViewIfNeeded();
            equipmentHeader.click();


            Locator singeingSection = childPage.locator("div")
                    .filter(new Locator.FilterOptions().setHas(equipmentHeader))
                    .last();


            Locator definitionField = singeingSection.locator("textarea, [role='textbox'], div[contenteditable='true']").first();


            definitionField.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            definitionField.click();

            definitionField.press("Control+A");
            definitionField.press("Backspace");

            definitionField.type("avg(^SINGEING.SINGED_METER)", new Locator.TypeOptions().setDelay(50));

            System.out.println("Section Visible: " + singeingSection.isVisible());
            System.out.println("Textboxes in this section: " + singeingSection.locator("textarea, [role='textbox']").count());


            childPage.selectOption("//div[8]//div[1]//div[1]//div[2]//div[1]//select[1]","meter");

            childPage.locator("button").filter(new Locator.FilterOptions().setHasText("VALIDATE")).first().click();

        }


         private  void editKPIImplementation(String SearchName) {

            childPage.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Search"))
                    .nth(1)
                    .click();

            childPage.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName("Search")).nth(1)
                    .fill(SearchName);

            Locator targetRow = childPage.locator("tr")
                    .filter(new Locator.FilterOptions().setHasText(SearchName));

            Locator expandBtn = targetRow.locator("#expandable-button");

            expandBtn.click();

            Locator machineHeader = childPage.locator("h4").filter(
                    new Locator.FilterOptions().setHas(childPage.locator("span.machine-name",
                            new Page.LocatorOptions().setHasText("SINGEING")))
            );

            machineHeader.scrollIntoViewIfNeeded();
            machineHeader.click();

            Locator singeingSectionn = childPage.locator("div")
                    .filter(new Locator.FilterOptions().setHas(machineHeader))
                    .last();

            Locator batchFrequencyDropdown = singeingSectionn.locator("select.select-batch-frequency");

            batchFrequencyDropdown.selectOption(new SelectOption().setLabel("Batch Start"));

            Locator unitDropdown = singeingSectionn.locator("select.select-unit");

            unitDropdown.selectOption(new SelectOption().setLabel("meter"));

            childPage.locator("input[value='2022-05-20']");

            childPage.check("div[class='machine-661'] input[name='active']");


            childPage.locator("div.btn-group.pull-right").locator("button").nth(1);


        }

}
