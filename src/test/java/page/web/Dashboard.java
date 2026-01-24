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


public class Dashboard extends BasePage {


    public Dashboard(Page page) {
        super(page);
    }

    private static final String chartGraph="//*[local-name()='g'][contains(@class,'highcharts-markers highcharts-series-0 highcharts-line-series highcharts-tracker')]//*[local-name()='path'][contains(@opacity,'1')]";
    private static final String dataToolTip="//*[name()='g'][contains(@class,'highcharts-label') and contains(@class,'highcharts-tooltip')]//*[local-name()='text']//*[local-name()='tspan']";
    Locator granularityL=page.locator("//span[text()='One Hour']//parent::button");


    public void createDashboard(String dashboardName, String description) throws InterruptedException {
        try {
            waitAndClick("//button[text()='Add Dashboard']");
            clickAndFill("input[id='title']", dashboardName);
            clickAndFill("input[id='description']", description);
            waitAndClick("//button[@role='combobox']");
            syncUntil(2000);
            waitAndClick("//div[@dir ='ltr']//following::*[text()='Public (Everyone)']");
            waitAndClick("//button[text()='Create']");
        } catch (Exception e) {
            System.out.println("Not able to create DashBoard: " + e);
        }
    }

    public void searchDashboard(String dashboardName) throws InterruptedException {
        try {
            page.getByPlaceholder("Search...").fill(dashboardName);
            waitAndClick("//a[text()='" + dashboardName + "']");
        } catch (Exception e) {
            System.out.println("Not able to search dashboard: " + e);
        }
    }

    public void openWidgetCreationPage() throws InterruptedException {
        try {
            waitAndClick("//span[text()='Add Widget']//parent::button");
            syncUntil(1000);
        } catch (Exception e) {
            System.out.println("Not able to open widget creation page: " + e);
        }
    }

    public void addEquipmentTrendWidget(String widgetType, String equipmentName, List<String> measureName, String time, String granularity) throws InterruptedException {
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Summary")).click();
            page.getByLabel("Global widget search").fill(widgetType);
            page.getByText(widgetType).click();
            //waitAndClick("//label[text()='Select Equipment']//following-sibling::div//child::button");
            page.getByText("Summary").click();
            page.getByPlaceholder("Search equipment...").fill(equipmentName);
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(equipmentName)).click();
            enterMeasureName(measureName);
            page.keyboard().press("Escape");
            //waitAndClick("//span[text()='Current']//parent::button");
            page.getByText("Current").click();
            page.getByText(time).click();
            if(time.equals("Last Month"))
                page.getByLabel(granularity).getByText(granularity).click();
            waitAndClick(page,granularityL,100);
            page.getByLabel(granularity).getByText(granularity).click();
        } catch (Exception e) {
            System.out.println("Not able to add equipment trend widget: " + e);
        }
    }

    public void createEquipmentStoppageDonutWidget(String widgetType, String equipmentName) throws InterruptedException {
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Summary")).click();
            syncUntil(2000);
            page.getByLabel("Global widget search").fill(widgetType);
            page.getByText(widgetType).click();
            page.click("//label[text()='Select Equipment']//following-sibling::div//child::button");
            page.getByPlaceholder("Search equipment...").fill(equipmentName);
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(equipmentName)).click();
        } catch (Exception e) {
            System.out.println("Not able to add EquipmentStoppageDonut widget: " + e);
        }
    }

    public void createEquipmentBatchDetailsWidget(String widgetType, String equipmentName) throws InterruptedException {
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Summary")).click();
            syncUntil(2000);
            page.getByLabel("Global widget search").fill(widgetType);
            page.getByText(widgetType).click();
            waitAndClick("//span[text()='Select equipment']//parent::button");
            page.getByPlaceholder("Search equipment...").fill(equipmentName);
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(equipmentName)).click();
        } catch (Exception e) {
            System.out.println("Not able to add equipment batch details widget: " + e);
        }
    }

    public void createBatchTrendWidget(String widgetType, String equipmentName, List<String> measureName) throws InterruptedException {
        try {

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Summary")).click();
            syncUntil(2000);
            page.getByLabel("Global widget search").fill(widgetType);
            page.getByText(widgetType).click();
            waitAndClick("//label[text()='Select Equipment']//following-sibling::div//child::button");
            page.getByPlaceholder("Search equipment...").fill(equipmentName);
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(equipmentName)).click();
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
            waitAndClick("//label[text()='Select Equipment']//following-sibling::div//child::button");
            page.getByPlaceholder("Search equipment...").fill(equipmentName);
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(equipmentName)).click();
            if (viewType.equals("Expanded")) {
                //page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Compact")).click();
                waitAndClick("//label[text()='View Mode']//following-sibling::button");
                page.getByText(viewType).click();
            }
            if (hideExpandCompactButton) {
                waitAndClick("//label[text()='Hide Expand/Compact Button']//preceding-sibling::button");
            }
        } catch (Exception e) {
            System.out.println("Not able to add equipment widget: " + e);
        }
    }

    public void saveTheWidget() {
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        } catch (Exception e) {
            System.out.println("Not able to save widget: " + e);
        }
    }

    public void deleteDashboard(String dashboardName) {
        try {
            clickAndFill("//input[@placeholder='Search...']", dashboardName);
            waitAndClick("//button[@data-state='closed']//preceding-sibling::div//child::a[text()='" + dashboardName + "']");
            waitAndClick("//div[text()='Delete']");
        } catch (Exception e) {
            System.out.println("Not able to delete widget: " + e);
        }
    }

    public Map<String,String> getChartDataLocally(int timeIndex,int dataIndex) throws InterruptedException {
        Map<String, String> graphData = new LinkedHashMap<>();
        waitForLocater(chartGraph);
        Locator noOfElements= page.locator(chartGraph);
        for(int i=0;i<noOfElements.count();i++){
            Locator firstPath =page.locator(chartGraph).nth(i);
            BoundingBox box = firstPath.boundingBox();
            if (box != null) {
                page.mouse().move(box.x - 5, box.y - 5);
                page.mouse().move(box.x + box.width / 2, box.y + box.height / 2);
            }
            Locator tSpans=page.locator(dataToolTip);
            String key = tSpans.nth(timeIndex).textContent().trim();
            String value = tSpans.nth(dataIndex).textContent().trim();
            graphData.put(key, ParseTheTimeFormat.formatStringTo2Decimal(value));
        }
        return graphData;
    }
}



