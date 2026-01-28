package page.web;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import base.web.BasePage;
import com.microsoft.playwright.options.AriaRole;
import utils.WaitUtils;
import java.util.List;


public class Dashboard extends BasePage {

    PageComponent pageComponent;
    private final Locator visibilityPublic;
    private final Locator addDashboardButton;
    private final Locator dashboardTitle;
    private final Locator dashboardDescription;
    private final Locator visibility;
    private final Locator createDashboard;
    private final Locator searchDashboard;
    private final Locator addWidget;
    private final Locator searchEquipment;
    private final Locator summaryButton;
    private final Locator widgetTypeSearchButton;
    private final Locator selectEquipment;
    private final Locator deleteDashboardMenu;
    private final Locator deleteButton;
    private final Locator saveWidget;
    private final Locator selectEquipmentInEq;
    private final Locator selectEquipmentInEqi;
    private final Locator viewType;
    private final Locator hideCompact;
    private final Locator time;
    private final Locator granularityL;

    public Dashboard(Page page) {
        super(page);
        pageComponent = new PageComponent(page);
        this.visibilityPublic = page.locator("//div[@dir ='ltr']//following::*[text()='Public (Everyone)']");
        this.addDashboardButton = getByRoleButton("Add Dashboard");
        this.dashboardTitle = getByPlaceholder("Enter dashboard title");
        this.dashboardDescription = getByPlaceholder("Enter dashboard description");
        this.visibility = page.locator("//button[@role='combobox']");
        this.createDashboard = getByRoleButton("Create");
        this.searchDashboard = getByPlaceholder("Search...");
        this.addWidget = getByRoleButton("Add Widget");
        this.searchEquipment = getByPlaceholder("Search equipment...");
        this.summaryButton = getByRoleButton("Summary");
        this.widgetTypeSearchButton = getByLabel("Global widget search");
        this.selectEquipment = page.locator("button").filter(new Locator.FilterOptions().setHasText("Select equipment"));
        this.deleteDashboardMenu = page.locator("(//button[@data-state='closed']//preceding-sibling::div//following::button)[6]");
        this.deleteButton = page.getByRole(AriaRole.MENU).getByRole(AriaRole.MENUITEM, new Locator.GetByRoleOptions().setName("Delete"));
        this.saveWidget = getByRoleButton("Save");
        this.selectEquipmentInEq = getByRoleOption("Equipment");
        this.selectEquipmentInEqi = getByLabelButton("Select Equipment");
        this.viewType = getByLabelButton("View Mode");
        this.hideCompact = getByLabelButtonSwitch("Hide Expand/Compact Button");
        this.time = getByLabelButton("Time");
        this.granularityL = getByLabelButton("Granularity");
    }

    public void createDashboard(String dashboardName, String description, String visibilityType) throws InterruptedException {
        addDashboardButton.click();
        dashboardTitle.fill(dashboardName);
        dashboardDescription.fill(description);
        if (visibilityType.equals("Public")) {
            //getByTextWithButtonParent("Private (Only you)");
            //getByRoleButton();
            waitAndClick(visibility);
            syncUntil(2000);
            waitAndClick(page, visibilityPublic, 2000);
        }
        createDashboard.click();
    }

    public void searchDashboard(String dashboardName) throws InterruptedException {
        searchDashboard.fill(dashboardName);
        getByText(dashboardName).click();
    }

    public void openWidgetCreationPage() throws InterruptedException {
        addWidget.click();
    }

    public void addEquipmentTrendWidget(String equipmentName, List<String> measureName, String timeV, String granularity) throws InterruptedException {
        selectWidgetType("Equipment Trend");
        selectEquipmentInEqi.click();
        searchEquipment.fill(equipmentName);
        getByRoleOption(equipmentName).click();
        pageComponent.enterMeasureName(measureName);
        page.keyboard().press("Escape");
        time.click();
        getByLabelAndText(timeV).click();
        if (timeV.equals("Last Month"))
            System.out.println("Eight hour is selected");
        granularityL.click();
        getByLabelAndText(granularity).click();
    }

    public void createEquipmentStoppageDonutWidget(String equipmentName) throws InterruptedException {
        selectWidgetType("Equipment Stoppage Donut");
        selectEquipmentName(equipmentName);
    }

    public void createEquipmentBatchDetailsWidget(String equipmentName) throws InterruptedException {
        selectWidgetType("Equipment Batch Details");
        selectEquipmentName(equipmentName);
    }

    public void createBatchTrendWidget(String equipmentName, List<String> measureName) throws InterruptedException {
        selectWidgetType("Batch Trend");
        selectEquipmentInEqi.click();
        searchEquipment.fill(equipmentName);
        getByRoleOption(equipmentName).click();
        pageComponent.enterMeasureName(measureName);
        page.keyboard().press("Escape");
    }

    public void createEquipmentWidget(String equipmentName, String viewTypeS, boolean hideExpandCompactButton) throws InterruptedException {
        summaryButton.click();
        WaitUtils.waitForVisible(widgetTypeSearchButton, 4000);
        widgetTypeSearchButton.fill("Equipment");
        selectEquipmentInEq.click();
        selectEquipmentInEqi.click();
        searchEquipment.fill(equipmentName);
        getByRoleOption(equipmentName).click();
        if (viewTypeS.equals("Expanded")) {
            viewType.click();
            page.getByText(viewTypeS).click();
        }
        if (hideExpandCompactButton) {
            //waitAndClick("//label[text()='Hide Expand/Compact Button']//preceding-sibling::button");
            hideCompact.click();
        }
    }

    public void saveTheWidget() {
        //page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        saveWidget.click();
    }

    public void deleteDashboard(String dashboardName) {
        searchDashboard.fill(dashboardName);
        waitAndClick(deleteDashboardMenu);
        //getByRoleButton();
        //getByText("Delete");
        deleteButton.click(new Locator.ClickOptions().setForce(true));
    }

    public void selectWidgetType(String widgetType) {
        summaryButton.click();
        widgetTypeSearchButton.fill(widgetType);
        getByText(widgetType).click();
    }

    public void selectEquipmentName(String equipmentName) {
        selectEquipment.click();
        searchEquipment.fill(equipmentName);
        getByRoleOption(equipmentName).click();
    }
}



