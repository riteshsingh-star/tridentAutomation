package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.WaitUtils;

import java.util.regex.Pattern;

public class KPIAdvanceImplementation extends BasePage {
    BrowserContext context;
    Page childPage;
    PageComponent pageComponent;
    private final Locator advancedImplementationPage;
    private final Locator kpiValidateSearch;
    private final Locator kpiSearch;
    private final Locator editKpiImplementationSearch;
    private final Locator editKpiSearch;
    private final Locator expandGlobalParameter;
    private final Locator selectUnit;
    private final Locator validateButton;
    private final Locator expandButton;
    private final Locator batchFrq;
    private final Locator unitSelect;
    private final Locator activeRadio;
    private final Locator textBox;
    private final Locator calender;
    private final Locator calenderInput;

    public KPIAdvanceImplementation(Page childPage, BrowserContext context) {
        super(childPage);
        this.context = context;
        this.childPage = childPage;
        this.advancedImplementationPage = childPage.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Advanced Implementation"));
        this.kpiValidateSearch = getByRoleButton("Search", childPage);
        this.kpiSearch = childPage.locator("//input[@aria-label='Search']");
        this.editKpiImplementationSearch = getByRoleButton("Search", childPage);
        this.editKpiSearch = childPage.locator("//input[@aria-label='Search']").last();
        this.expandGlobalParameter = childPage.locator("//tr[contains(@id,'MUIDataTableBodyRow')]");
        this.selectUnit = childPage.locator("//select[@class='multipleSelDD searchSelect form-control select-unit']");
        this.validateButton = childPage.locator("//button[text()='VALIDATE']");
        this.expandButton = childPage.locator("#expandable-button");
        this.batchFrq = childPage.locator("select.select-frequency");
        this.unitSelect = childPage.locator("select.select-unit");
        this.activeRadio = childPage.locator("//label[text()='Active']");
        this.textBox = childPage.locator("textarea, [role='textbox'], div[contenteditable='true']");
        this.calender = childPage.locator("div.calendarWrap");
        this.calenderInput = childPage.locator("input[type='date']");

    }

    public void addLogicToTheKPIAndValidate(String globalParameterName, String kpiName, String machineName, String date, String batchFreq, String units, String formula) {
        WaitUtils.waitForEnabled(childPage,advancedImplementationPage,4000);
        advancedImplementationPage.click();
        advancedImplementationPage.click();
        validateGlobalParameterImplementations(globalParameterName, machineName, units, formula);
        validateKPIImplementation(kpiName, machineName, date, batchFreq, units);
    }

    private void validateGlobalParameterImplementations(String globalParameterName, String machineName, String unit, String formula) {
        kpiValidateSearch.first().click();
        kpiSearch.fill(globalParameterName);
        expandGlobalParameter.first().click();
        Locator equipmentHeader = childPage.locator("h4").locator("span").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^" + machineName + "$")));
        equipmentHeader.scrollIntoViewIfNeeded();
        equipmentHeader.click();
        Locator machineSection = childPage.locator("div").filter(new Locator.FilterOptions().setHas(equipmentHeader)).last();
        Locator definitionField = machineSection.locator(textBox).first();
        definitionField.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        definitionField.click();
        childPage.keyboard().press("Enter");
        definitionField.type(formula, new Locator.TypeOptions().setDelay(50));
        machineSection.locator(selectUnit).selectOption(unit);
        machineSection.locator(validateButton).first().click();
    }

    private void validateKPIImplementation(String kpiName, String machineName, String date, String batchFrequency, String unit) {
        editKpiImplementationSearch.nth(1).click();
        editKpiSearch.fill(kpiName);
        Locator targetRow = childPage.locator("tr").filter(new Locator.FilterOptions().setHasText(kpiName));
        Locator expandBtn = targetRow.locator(expandButton);
        expandBtn.click();
        Locator machineHeader=childPage.locator("h4").locator("span").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^" + machineName + "$")));
        machineHeader.scrollIntoViewIfNeeded();
        machineHeader.click();
        Locator machineSection = childPage.locator("div").filter(new Locator.FilterOptions().setHas(machineHeader)).last();
        Locator batchFrequencyDropdown = machineSection.locator(batchFrq);
        batchFrequencyDropdown.selectOption(batchFrequency);
        Locator unitDropdown = machineSection.locator(unitSelect);
        unitDropdown.selectOption(new SelectOption().setLabel(unit));
        machineSection.locator(calender).locator(calenderInput).fill(date);
        machineSection.locator(activeRadio).check();
        machineSection.locator(validateButton).first().click();
    }

}
