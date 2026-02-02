package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import utils.WaitUtils;

public class CreateNewKPIDefinition extends BasePage {

    BrowserContext context;
    Page childPage;
    PageComponent pageComponent;

    private final Locator createKpi;
    private final Locator kpiNameField;
    private final Locator kpiIndustry;
    private final Locator kpiType;
    private final Locator kpiContinueButton;
    private final Locator globalParameterButton;
    private final Locator selectGlobalParameter;
    private final Locator textboxKpivalidate;
    private final Locator variblePrecession;
    private final Locator variableValidate;
    private final Locator editKPIPage;
    private final Locator searcheditKPI;
    private final Locator searchInput;
    private final Locator editButton;
    private final Locator aggregateFunctionDropdown;
    private final Locator kpiPerformanceDropdown;
    private final Locator unitsDropdown;



    public CreateNewKPIDefinition(Page childPage, BrowserContext context) {
        super(childPage);
        this.context=context;
        this.childPage=childPage;
        this.createKpi = getByText("New KPI Definition", childPage);
        this.kpiNameField = getByPlaceholder("KPI Name", childPage);
        this.kpiIndustry = getByLabel("Aluminium", childPage);
        this.kpiType = getByLabel("Batch", childPage);
        this.kpiContinueButton = getByRoleButton("Continue", childPage);
        this.globalParameterButton = childPage.locator("//li[.//span[normalize-space()='Global Parameters']]");
        this.selectGlobalParameter=childPage.locator("p[draggable='true']", new Page.LocatorOptions().setHasText("New_Test_Batch"));
        this.textboxKpivalidate = getByPlaceholder("Drag and Drop fields to define the KPI...", childPage);
        this.variblePrecession = getByPlaceholder("Enter Precision", childPage);
        this.variableValidate = getByRoleButton("Validate", childPage);
        this.editKPIPage = childPage.getByText("KPI Definitions List");
        this.searcheditKPI = childPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
        this.searchInput = childPage.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.editButton = childPage.getByTitle("Edit").first();
        this.aggregateFunctionDropdown = childPage.locator("//label[normalize-space()='Select Aggregated Function']/following-sibling::div//select");
        this.kpiPerformanceDropdown = childPage.locator("//label[normalize-space()='KPI Performance']/following-sibling::div//select");
        this.unitsDropdown = childPage.locator("//label[normalize-space()='Units']/following-sibling::div//select");
    }


    public void dragNewBatchBatchToDefinition() {
        Locator source = selectGlobalParameter;
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
        selectVariableAndDefineKPI("SUM","More Value is Good","meter","2");
        variableValidate.click();

    }

    public void editExistingKPI(String searchName) throws InterruptedException {
        editKPIPage.click();
        searcheditKPI.click();
        searchInput.fill(searchName);
        editButton.click();
        kpiContinueButton.click();
        String classes = globalParameterButton.getAttribute("class");
        if (classes == null || !classes.contains("active")) {
            globalParameterButton.click(new Locator.ClickOptions().setForce(true));
        }
        syncUntil(2000);
        globalParameterButton.click();
        dragNewBatchBatchToDefinition();
        selectVariableAndDefineKPI("SUM", "More Value is Good", "meter", "2");
        variableValidate.click();


    }

    public void selectVariableAndDefineKPI(String aggregateType, String KPIPerformanceCriteria, String units, String precision) throws InterruptedException {
        aggregateFunctionDropdown.selectOption(new SelectOption().setValue(aggregateType));
        kpiPerformanceDropdown.selectOption(KPIPerformanceCriteria);
        unitsDropdown.selectOption(units);
        variblePrecession.fill(precision);

    }
}
