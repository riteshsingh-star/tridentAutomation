package page.web;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import base.web.BasePage;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;
import com.trident.playwright.utils.ParseTheTimeFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class Dashboard extends BasePage {


    public Dashboard(Page page) {
        super(page);
    }

    private static final String chartGraph="//*[local-name()='g'][contains(@class,'highcharts-markers highcharts-series-0 highcharts-line-series highcharts-tracker')]//*[local-name()='path'][contains(@opacity,'1')]";
    private static final String dataToolTip="//*[name()='g'][contains(@class,'highcharts-label') and contains(@class,'highcharts-tooltip')]//*[local-name()='text']//*[local-name()='tspan']";
    Locator granularityL=page.locator("//span[text()='One Hour']//parent::button");
    Locator visibilityPublic=page.locator("//div[@dir ='ltr']//following::*[text()='Public (Everyone)']");


    public void createDashboard(String dashboardName, String description, String visibilityType) throws InterruptedException {
        try {
            getByRoleButton("Add Dashboard");
            getByPlaceholder("Enter dashboard title", dashboardName);
            getByPlaceholder("Enter dashboard description", description);
            if(visibilityType.equals("Public")){
                //getByTextWithButtonParent("Private (Only you)");
                //getByRoleButton();
                waitAndClick("//button[@role='combobox']");
                syncUntil(2000);
                waitAndClick(page, visibilityPublic,2000);
            }
            getByRoleButton("Create");
        } catch (Exception e) {
            System.out.println("Not able to create DashBoard: " + e);
        }
    }

    public void searchDashboard(String dashboardName) throws InterruptedException {
        try {
            getByPlaceholder("Search...", dashboardName);
            getByText(dashboardName);
        } catch (Exception e) {
            System.out.println("Not able to search dashboard: " + e);
        }
    }

    public void openWidgetCreationPage() throws InterruptedException {
        try {
            getByRoleButton("Add Widget");
        } catch (Exception e) {
            System.out.println("Not able to open widget creation page: " + e);
        }
    }

    public void addEquipmentTrendWidget(String widgetType, String equipmentName, List<String> measureName, String time, String granularity) throws InterruptedException {
        try {
            selectWidgetType(widgetType);
            getByLabelButton("Select Equipment");
            getByPlaceholder("Search equipment...", equipmentName);
            getByRoleOption(equipmentName);
            enterMeasureName(measureName);
            page.keyboard().press("Escape");
            getByLabelButton("Time");
            getByLabelAndText(time);
            if(time.equals("Last Month"))
                System.out.println("Eight hour is selected");
            getByLabelButton("Granularity");
            getByLabelAndText(granularity);
        } catch (Exception e) {
            System.out.println("Not able to add equipment trend widget: " + e);
        }
    }

    public void createEquipmentStoppageDonutWidget(String widgetType, String equipmentName) throws InterruptedException {
        try {
            selectWidgetType(widgetType);
            selectEquipmentName(equipmentName);
        } catch (Exception e) {
            System.out.println("Not able to add EquipmentStoppageDonut widget: " + e);
        }
    }

    public void createEquipmentBatchDetailsWidget(String widgetType, String equipmentName) throws InterruptedException {
        try {
            selectWidgetType(widgetType);
            selectEquipmentName(equipmentName);
        } catch (Exception e) {
            System.out.println("Not able to add equipment batch details widget: " + e);
        }
    }

    public void createBatchTrendWidget(String widgetType, String equipmentName, List<String> measureName) throws InterruptedException {
        try {
            selectWidgetType(widgetType);
            getByLabelButton("Select Equipment");
            getByPlaceholder("Search equipment...", equipmentName);
            getByRoleOption(equipmentName);
            enterMeasureName(measureName);
            page.keyboard().press("Escape");
        } catch (Exception e) {
            System.out.println("Not able to add batch trend widget: " + e);
        }
    }

    public void createEquipmentWidget(String widgetType, String equipmentName, String viewType, boolean hideExpandCompactButton) throws InterruptedException {
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Summary")).click();
            syncUntil(2000);
            page.getByLabel("Global widget search").fill(widgetType);
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Equipment").setExact(true)).click();
            getByLabelButton("Select Equipment");
            getByPlaceholder("Search equipment...", equipmentName);
            getByRoleOption(equipmentName);
            if (viewType.equals("Expanded")) {
                getByLabelButton("View Mode");
                page.getByText(viewType).click();
            }
            if (hideExpandCompactButton) {
                //waitAndClick("//label[text()='Hide Expand/Compact Button']//preceding-sibling::button");
                getByLabelButtonSwitch("Hide Expand/Compact Button");
            }
        } catch (Exception e) {
            System.out.println("Not able to add equipment widget: " + e);
        }
    }

    public void saveTheWidget() {
        try {
            //page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
            getByRoleButton("Save");
        } catch (Exception e) {
            System.out.println("Not able to save widget: " + e);
        }
    }

    public void deleteDashboard(String dashboardName) {
        try {
            getByPlaceholder("Search...", dashboardName);
            waitAndClick("(//button[@data-state='closed']//preceding-sibling::div//following::button)[6]");
            //getByRoleButton();
            //getByText("Delete");
            page.getByRole(AriaRole.MENU)
                    .getByRole(AriaRole.MENUITEM, new Locator.GetByRoleOptions().setName("Delete"))
                    .click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            System.out.println("Not able to delete widget: " + e);
        }
    }

    public void selectWidgetType(String widgetType){
        getByRoleButton("Summary");
        getByLabel("Global widget search",widgetType);
        getByText(widgetType);
    }

    public void selectEquipmentName(String equipmentName){
        page.locator("button").filter(new Locator.FilterOptions().setHasText("Select equipment")).click();
        getByPlaceholder("Search equipment...", equipmentName);
        getByRoleOption(equipmentName);

    }
}



