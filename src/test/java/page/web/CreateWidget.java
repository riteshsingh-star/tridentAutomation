package page.web;

import base.web.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Allure;
import utils.WaitUtils;

import java.util.List;

public class CreateWidget extends BasePage {

    PageComponent pageComponent;
    private final Locator addWidget;
    private final Locator searchEquipment;
    private final Locator summaryButton;
    private final Locator widgetTypeSearchButton;
    private final Locator selectEquipment;
    private final Locator saveWidget;
    private final Locator selectEquipmentInEq;
    private final Locator selectEquipmentInEqi;
    private final Locator viewType;
    private final Locator hideCompact;
    private final Locator time;
    private final Locator granularityL;
    private final Locator editWidgetButton;
    private final Locator deleteWidgetHamburger;
    private final Locator deleteButton;
    private final Locator resizableHandle;
    private final Locator equipmentTrend;
    private final Locator machineNameOnWidget;
    private final Locator equipmentStoppage;
    private final Locator equipmentBatchDetail;
    private final Locator batchDetail;
    private final Locator reactBackground;

    public CreateWidget(Page page) {
        super(page);
        pageComponent = new PageComponent(page);
        this.addWidget = getByRoleButton("Add Widget", page);
        this.searchEquipment = getByPlaceholder("Search equipment...", page);
        this.summaryButton = getByRoleButton("Summary", page);
        this.widgetTypeSearchButton = getByLabel("Global widget search", page);
        this.selectEquipment = page.locator("button").filter(new Locator.FilterOptions().setHasText("Select equipment"));
        this.saveWidget = getByRoleButton("Save", page);
        this.selectEquipmentInEq = getByRoleOption("Equipment", page);
        this.selectEquipmentInEqi = getByLabelButton("Select Equipment", page);
        this.viewType = getByLabelButton("View Mode", page);
        this.hideCompact = getByLabelButtonSwitch("Hide Expand/Compact Button", page);
        this.time = getByLabelButton("Time", page);
        this.granularityL = getByLabelButton("Granularity", page);
        this.editWidgetButton = getByAltText("edit", page);
        this.deleteWidgetHamburger = getByAltText("options", page);
        this.deleteButton = page.getByRole(AriaRole.MENU).getByRole(AriaRole.MENUITEM, new Locator.GetByRoleOptions().setName("Delete"));
        this.resizableHandle = page.locator(".ui-resizable-se").last();
        this.equipmentTrend = page.locator("span").filter(new Locator.FilterOptions().setHasText("Equipment Performance"));
        this.machineNameOnWidget = getByRoleLink("SINGEING", page);
        this.equipmentStoppage= page.locator("h3").filter(new Locator.FilterOptions().setHasText("Equipment Runtime"));
        this.equipmentBatchDetail=page.locator("span").filter(new Locator.FilterOptions().setHasText("Equipment Batch Details"));
        this.batchDetail=page.locator("span").filter(new Locator.FilterOptions().setHasText("Batch Trend"));
        this.reactBackground = page.locator("//*[local-name()='rect' and contains(@class,'highcharts-plot-background')]");
    }

    public void openWidgetCreationPage(){
        Allure.step("Adding Widget in Dashboard");
        addWidget.click();
    }

    public String addEquipmentTrendWidget(String equipmentName, List<String> measureName, String timeV, String granularity){
        Allure.step("Creating Equipment Trend Widget");
        selectWidgetType("Equipment Trend");
        selectEquipmentInEqi.click();
        searchEquipment.fill(equipmentName);
        getByRoleOption(equipmentName, page).click();
        pageComponent.enterMeasureName(measureName);
        page.keyboard().press("Escape");
        time.click();
        getByLabelAndText(timeV, page).click();
        if (timeV.equals("Last Month"))
            System.out.println("Eight hour is selected");
        granularityL.click();
        getByLabelAndText(granularity, page).click();
        saveTheWidget();
        dragTheChart();
        String widgetName=equipmentTrend.textContent();
        String machineName=machineNameOnWidget.textContent();
        return widgetName+" "+machineName;
    }

    public String createEquipmentStoppageDonutWidget(String equipmentName){
        Allure.step("Creating Equipment Stoppage Donut Widget");
        selectWidgetType("Equipment Stoppage Donut");
        selectEquipmentName(equipmentName);
        saveTheWidget();
        String widgetName=equipmentStoppage.textContent();
        String machineName=machineNameOnWidget.textContent();

        return widgetName+" "+machineName;
    }

    public String createEquipmentBatchDetailsWidget(String equipmentName){
        Allure.step("Creating Equipment Batch Details Widget");
        selectWidgetType("Equipment Batch Details");
        selectEquipmentName(equipmentName);
        saveTheWidget();
        String  widgetName=equipmentBatchDetail.textContent();
        String machineName=machineNameOnWidget.textContent();
        return widgetName+" "+machineName;
    }

    public String createBatchTrendWidget(String equipmentName, List<String> measureName){
        Allure.step("Creating Batch Trend Widget");
        selectWidgetType("Batch Trend");
        selectEquipmentInEqi.click();
        searchEquipment.fill(equipmentName);
        getByRoleOption(equipmentName, page).click();
        pageComponent.enterMeasureName(measureName);
        page.keyboard().press("Escape");
        saveTheWidget();
        String widgetName=batchDetail.textContent();
        String machineName=machineNameOnWidget.textContent();
        return widgetName+" "+machineName;
    }

    public String createEquipmentWidget(String equipmentName, String viewTypeS, boolean hideExpandCompactButton){
        Allure.step("Creating Equipment Widget");
        summaryButton.click();
        WaitUtils.waitForVisible(widgetTypeSearchButton, 4000);
        widgetTypeSearchButton.fill("Equipment");
        selectEquipmentInEq.click();
        selectEquipmentInEqi.click();
        searchEquipment.fill(equipmentName);
        getByRoleOption(equipmentName, page).click();
        if (viewTypeS.equals("Expanded")) {
            viewType.click();
            page.getByText(viewTypeS).click();
        }
        if (hideExpandCompactButton) {
            hideCompact.click();
        }

        String widgetName=equipmentStoppage.textContent();
        String machineName=machineNameOnWidget.textContent();
        return widgetName+" "+machineName;
    }

    public void saveTheWidget() {
        //page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        Allure.step("Saving Widget");
        saveWidget.click();
    }

    public void selectWidgetType(String widgetType) {
        Allure.step("Selecting Widget Type");
        summaryButton.click();
        widgetTypeSearchButton.fill(widgetType);
        getByText(widgetType, page).click();
    }

    public void selectEquipmentName(String equipmentName) {
        Allure.step("Selecting Equipment Name");
        selectEquipment.click();
        searchEquipment.fill(equipmentName);
        getByRoleOption(equipmentName, page).click();
    }

    public void editWidget(int indexOfWidgetToEdit) {
        Allure.step("Editing Widget");
        editWidgetButton.nth(indexOfWidgetToEdit).click();
    }

    public void deleteWidget(int indexOfWidgetToDelete) {
        Allure.step("Deleting Widget");
        deleteWidgetHamburger.nth(indexOfWidgetToDelete).click();
        deleteButton.click();
    }

    public void editEquipmentNameInWidget(String equipmentName){
        Allure.step("Editing Widget Configuration");
        searchEquipment.fill(equipmentName);
    }

    public void dragTheChart(){
        Locator widget = page
                .locator(".grid-stack-item-content")
                .filter(new Locator.FilterOptions()
                        .setHasText("Equipment Performance"));
        widget.hover();
        pageComponent.dragTheChartGraph(resizableHandle,page);
    }
}
