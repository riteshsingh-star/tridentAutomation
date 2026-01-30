package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import utils.WaitUtils;

import java.util.regex.Pattern;


public class CreateAdminFlow extends BasePage {

    BrowserContext context;
    Page childPage;
    PageComponent pageComponent;
    private final Locator globalParameterList;
    private final Locator createNewGlobalParameter;
    private final Locator globalParameterName;
    private final Locator globalParameterDataType;
    private final Locator globalParameterDataTypeValueDouble;
    private final Locator globalParameterDimension;
    private final Locator globalParameterSave;
    private final Locator createKpi;
    private final Locator kpiNameField;
    private final Locator kpiIndustry;
    private final Locator kpiType;
    private final Locator kpiContinueButton;
    private final Locator globalParameterButton;
    private final Locator selectGlobalparameter;
    private final Locator textboxKpivalidate;
    private final Locator variblePrecession;
    private final Locator variableValidate;
    private final Locator editKPIPage;
    private final Locator searcheditKPI;
    private final Locator searchInput;
    private final Locator editButton;
    private final Locator kpiValidatePage;
    private final Locator kpiValidateSearch;
    private final Locator kpiSearch;
    private final Locator editKpiImplementationSearch;
    private final Locator editKpiSearch;
    private final String kpiAggregate;
    private final String kpiPerformanceCriteria;
    private final String kpiUnit;
    private final Locator expandGlobalParameter;

    public CreateAdminFlow(Page page, BrowserContext context) {
        super(page);
        pageComponent = new PageComponent(page);
        this.context = context;
        this.childPage = pageComponent.moveToAdminPage(page, context);
        this.globalParameterList = getByText("Global Parameters List", childPage);
        this.createNewGlobalParameter = getByRoleButton("CREATE NEW", childPage);
        this.globalParameterName = getByPlaceholder("Global Parameter Name", childPage);
        this.globalParameterDataType = childPage.locator("//div[@class=' css-tlfecz-indicatorContainer']");
        this.globalParameterDataTypeValueDouble = childPage.locator("div[class*='menu']").getByText("Double", new Locator.GetByTextOptions().setExact(true));
        this.globalParameterDimension = getByLabelCheckbox("Batch", childPage);
        this.globalParameterSave = getByRoleLabelText(childPage, "Save");
        this.createKpi = getByText("New KPI Definition", childPage);
        this.kpiNameField = getByPlaceholder("KPI Name", childPage);
        this.kpiIndustry = getByLabel("Aluminium", childPage);
        this.kpiType = getByLabel("Batch", childPage);
        this.kpiContinueButton = getByRoleButton("Continue", childPage);
        this.globalParameterButton = childPage.getByText("Global Parameters").first();
        this.selectGlobalparameter = childPage.getByText("New_Test_Batch", new Page.GetByTextOptions().setExact(true));
        this.textboxKpivalidate = getByPlaceholder("Drag and Drop fields to define the KPI...", childPage);
        this.variblePrecession = getByPlaceholder("Enter Precision", childPage);
        this.variableValidate = getByRoleButton("Validate", childPage);
        this.editKPIPage = childPage.getByText("KPI Definitions List");
        this.searcheditKPI = childPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
        this.searchInput = childPage.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.editButton = childPage.getByTitle("Edit");
        this.kpiValidatePage = childPage.getByText("Advanced Implementation");
        this.kpiValidateSearch = getByRoleButton("Search", childPage);
        this.kpiSearch = childPage.locator("//input[@aria-label='Search']");
        //this.kpiSearch=getByLabel("Search",childPage);
        this.editKpiImplementationSearch = getByRoleButton("Search", childPage);
        this.editKpiSearch = childPage.locator("//input[@aria-label='Search']");
        this.kpiAggregate = "#fromUnit";
        this.kpiPerformanceCriteria = "(//select[@id='fromUnit'])[2]";
        this.kpiUnit ="(//select[@id='fromUnit'])[3]";
        this.expandGlobalParameter=childPage.locator("//tr[contains(@id,'MUIDataTableBodyRow')]");
    }

    public void createGlobalParameter(String name) throws InterruptedException {
        clickGlobalParameters();
        clickCreateNewParameter(name);
        clickSaveButton();
    }

    private void clickGlobalParameters() {
        globalParameterList.click();
    }

    private void clickCreateNewParameter(String name) {

        createNewGlobalParameter.click();
        globalParameterName.fill(name);
        globalParameterDataType.click();
        globalParameterDataTypeValueDouble.click();
        globalParameterDimension.check();
    }

    private void clickSaveButton() {
        globalParameterSave.click();
    }

    public void dragNewBatchBatchToDefinition() {
        Locator source = selectGlobalparameter;
        Locator target = textboxKpivalidate;
        source.scrollIntoViewIfNeeded();
        target.scrollIntoViewIfNeeded();
        target.click();
        childPage.keyboard().press("End");
        source.dragTo(target);

    }

    public void createNewKPIDefinition(String name) throws InterruptedException {
        createKpi.click();
        defineKpi(name);
        selectVariableAndDefine();
    }

    private void defineKpi(String kpiName) {
        kpiNameField.fill(kpiName);
        childPage.selectOption("#selectedPlant", "covacsis_dev");
        kpiIndustry.check();
        kpiType.check();
        kpiContinueButton.click();
    }

    private void selectVariableAndDefine() throws InterruptedException {
        WaitUtils.waitForVisible(globalParameterButton,4000);
        globalParameterButton.click();
        dragNewBatchBatchToDefinition();
        /*childPage.getByLabel("Aggregation").selectOption(new SelectOption().setLabel("Sum"));
        //childPage.selectOption("#fromUnit", new SelectOption().setValue("SUM"));
        // childPage.selectOption("(//select[@id='fromUnit'])[2]", "More Value is Good");
        childPage.getByLabel("From Unit").selectOption(new SelectOption().setLabel("More Value is Good"));
        //childPage.selectOption("(//select[@id='fromUnit'])[3]", "meter");

        childPage.getByLabel("Unit").selectOption(new SelectOption().setLabel("meter"));
        variblePrecession.fill("2");
        variableValidate.click();*/

        selectVariableAndDefineKPI("SUM","More Value is Good","meter","2");
        variableValidate.click();

    }

    public void editExistingKPI() throws InterruptedException {
        editKPIPage.click();
        searcheditKPI.click();
        searchInput.fill("New_Test_Batch_KPI");
        editButton.click();
        kpiContinueButton.click();
        WaitUtils.waitForVisible(globalParameterButton,4000);
        globalParameterButton.click();
        dragNewBatchBatchToDefinition();
      /*  //Locator aggreagte=childPage.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^SelectSumAverageAverage By DurationAverage By KpiCumulative$"))).locator("#fromUnit").scrollIntoViewIfNeeded();
        //childPage.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^SelectSumAverageAverage By DurationAverage By KpiCumulative$"))).locator("#fromUnit").click();
        //childPage.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Sum")).click();
        //childPage.getByText("Sum", new Page.GetByTextOptions().setExact(true)).click();
        syncUntil(5000);
        childPage.selectOption("#fromUnit", new SelectOption().setValue("SUM"));
        childPage.selectOption("(//select[@id='fromUnit'])[2]", "More Value is Good");
        //childPage.getByLabel("From Unit").selectOption(new SelectOption().setLabel("More Value is Good"));
        //childPage.selectOption("(//select[@id='fromUnit'])[3]", "meter");
        syncUntil(5000);
        childPage.getByLabel("Unit").selectOption(new SelectOption().setLabel("meter"));
        variblePrecession.fill("2");*/
        //page.locator(kpiAggregate).scrollIntoViewIfNeeded();
        selectVariableAndDefineKPI("SUM", "More Value is Good", "meter", "2");
        variableValidate.click();
        syncUntil(5000);

    }

    public void addLogicToTheKPIAndValidate(String name, String text, String searchName,String machineName, String date, String batchFreq, String units) throws InterruptedException {
        kpiValidatePage.click();
        editGlobalParameterImplementations(name, text,"SINGEING");
        editKPIImplementation(searchName,machineName,date,batchFreq,units);
    }

    private void editGlobalParameterImplementations(String globalParameterName, String machineName, String unit) {
        kpiValidateSearch.first().click();
        kpiSearch.fill(globalParameterName);
        expandGlobalParameter.first().click();
        Locator equipmentHeader = childPage.locator("h4").filter(new Locator.FilterOptions().setHasText(machineName));
        equipmentHeader.scrollIntoViewIfNeeded();
        equipmentHeader.click();
        Locator machineSection = childPage.locator("div").filter(new Locator.FilterOptions().setHas(equipmentHeader))
                .last();
        Locator definitionField = machineSection.locator("textarea, [role='textbox'], div[contenteditable='true']").first();
        definitionField.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        definitionField.click();
        //definitionField.press("Control+A");
        //childPage.keyboard().press("Backspace");
        childPage.keyboard().press("Enter");
        definitionField.type("avg(^SINGEING.SINGED_METER)", new Locator.TypeOptions().setDelay(50));
        //System.out.println("Section Visible: " + machineSection.isVisible());
        //System.out.println("Textboxes in this section: " + machineSection.locator("textarea, [role='textbox']").count());
        //childPage.selectOption("(//span[text()='"+machineName+"']//following::select)[1]", "meter");
        //childPage.locator("//span[text()='"+machineName+"']//following::button[text()='VALIDATE']").first().click();
        machineSection.locator("//select[@class='multipleSelDD searchSelect form-control select-unit']").selectOption(unit);
        machineSection.locator("//button[text()='VALIDATE']").first().click();
    }

    private void editKPIImplementation(String SearchName, String machineName, String date, String batchFrequency, String unit) {
        editKpiImplementationSearch.nth(1).click();
        editKpiSearch.nth(1).fill(SearchName);
        childPage.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).nth(1).fill(SearchName);
        Locator targetRow = childPage.locator("tr").filter(new Locator.FilterOptions().setHasText(SearchName));
        Locator expandBtn = targetRow.locator("#expandable-button");
        expandBtn.click();
        Locator machineHeader = childPage.locator("h4").filter(new Locator.FilterOptions().setHas(childPage.locator("span.machine-name",
                                new Page.LocatorOptions().setHasText(Pattern.compile("^"+machineName+"$")))));
        machineHeader.scrollIntoViewIfNeeded();
        machineHeader.click();
        Locator singeingSection = childPage.locator("div").filter(new Locator.FilterOptions().setHas(machineHeader)).last();
        Locator batchFrequencyDropdown = singeingSection.locator("select.select-batch-frequency");
        batchFrequencyDropdown.selectOption(new SelectOption().setLabel(batchFrequency));
        Locator unitDropdown = singeingSection.locator("select.select-unit");
        unitDropdown.selectOption(new SelectOption().setLabel(unit));
       singeingSection.locator("div.calendarWrap").locator("input[type='date']").fill(date);
        //childPage.locator("input[value='2022-05-20']");
        //page.locator("div").filter(new Locator.FilterOptions().setHasText("SINGEING")).locator("input.custom_datepicker").first().fill("2026-01-08");
        //page.locator("input.custom_datepicker").fill("2026-01-15");
        //childPage.check("div[class='machine-661'] input[name='active']");
        singeingSection.locator("//label[text()='Active']").check();
        //childPage.locator("div.btn-group.pull-right").locator("button").nth(1);
        singeingSection.locator("//button[text()='VALIDATE']").first().click();
    }

    public void selectVariableAndDefineKPI(String aggregateType, String KPIPerformanceCriteria, String units, String precision) throws InterruptedException {
        //childPage.selectOption("#fromUnit", new SelectOption().setValue(aggregateType));
        childPage.selectOption("//label[contains(text(),'Select Aggregated Function')]//following-sibling::div//child::select",new SelectOption().setValue(aggregateType));
        //childPage.selectOption("(//select[@id='fromUnit'])[2]", KPIPerformanceCriteria);
        childPage.selectOption("//label[contains(text(),'KPI Performance')]//following-sibling::div//child::select",KPIPerformanceCriteria);
        //childPage.selectOption("(//select[@id='fromUnit'])[3]", units);
        childPage.selectOption("//label[contains(text(),'Units')]//following-sibling::div//child::select",units);
        variblePrecession.fill(precision);

    }
}
