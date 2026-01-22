package page.web;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import base.web.BasePage;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;
import org.testng.annotations.Test;
import page.api.GetCharDataApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Dashboard extends BasePage {


    public Dashboard(Page page) {
        super(page);
    }
    public void createDashboard(String dashboardName, String description) throws InterruptedException {
        waitAndClick("//button[text()='Add Dashboard']");
        clickAndFill("input[id='title']", dashboardName);
        clickAndFill("input[id='description']", description);
        waitAndClick("//button[@role='combobox']");
        syncUntil(2000);
        waitAndClick("//div[@dir ='ltr']//following::*[text()='Public (Everyone)']");
        waitAndClick("//button[text()='Create']");
    }

    public  void searchDashboard(String dashboardName) throws InterruptedException {
        clickAndFill("//input[@placeholder='Search...']",  dashboardName);
        waitAndClick("//a[text()='"+dashboardName+"']");
    }

    public void openWidgetCreationPage() throws InterruptedException {
        waitAndClick("//span[text()='Add Widget']//parent::button");
        syncUntil(1000);
    }

    public void addEquipmentTrendWidget(String widgetType, String equipmentName, List<String> measureName, String time, String granularity) throws InterruptedException {
        //page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Add Widget")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Summary")).click();
        page.getByLabel("Global widget search").fill(widgetType);
        page.getByText(widgetType).click();
        waitAndClick("//label[text()='Select Equipment']//following-sibling::div//child::button");
        page.getByPlaceholder("Search equipment...").fill(equipmentName);
        page.getByText(equipmentName).click();
        enterMeasureName(measureName);
        page.keyboard().press("Escape");
        waitAndClick("//span[text()='Current']//parent::button");
        page.getByText(time).click();
        waitAndClick("//span[text()='One Hour']//parent::button");
        page.getByText(granularity).click();
        waitAndClick("//label[text()='Merge All']//following-sibling::button");
        waitAndClick("//label[text()='Show Overview']//following-sibling::button");

    }

    public void createEquipmentStoppageDonutWidget(String widgetType, String equipmentName) throws InterruptedException {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Summary")).click();
        syncUntil(2000);
        page.getByLabel("Global widget search").fill(widgetType);
        page.getByText(widgetType).click();
        page.click("//label[text()='Select Equipment']//following-sibling::div//child::button");
        page.getByPlaceholder("Search equipment...").fill(equipmentName);
        page.getByText(equipmentName).click();
    }

    public void createEquipmentBatchDetailsWidget(String widgetType, String equipmentName) throws InterruptedException {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Summary")).click();
        syncUntil(2000);
        page.getByLabel("Global widget search").fill(widgetType);
        page.getByText(widgetType).click();
        waitAndClick("//span[text()='Select equipment']//parent::button");
        page.getByPlaceholder("Search equipment...").fill(equipmentName);
        page.getByText(equipmentName).click();
    }

    public void createBatchTrendWidget(String widgetType, String equipmentName, List<String> measureName) throws InterruptedException {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Summary")).click();
        syncUntil(2000);
        page.getByLabel("Global widget search").fill(widgetType);
        page.getByText(widgetType).click();
        waitAndClick("//label[text()='Select Equipment']//following-sibling::div//child::button");
        page.getByPlaceholder("Search equipment...").fill(equipmentName);
        page.getByText(equipmentName).click();
        enterMeasureName(measureName);
        page.keyboard().press("Escape");
    }

    public void createEquipmentWidget(String widgetType, String equipmentName, String viewType, boolean hideExpandCompactButton) throws InterruptedException {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Summary")).click();
        syncUntil(2000);
        page.getByLabel("Global widget search").fill(widgetType);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Equipment").setExact(true)).click();
        waitAndClick("//label[text()='Select Equipment']//following-sibling::div//child::button");
        page.getByPlaceholder("Search equipment...").fill(equipmentName);
        page.getByText(equipmentName).click();
        if(viewType.equals("Expanded")){
            //page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Compact")).click();
            waitAndClick("//label[text()='View Mode']//following-sibling::button");
            page.getByText(viewType).click();
        }
        if(hideExpandCompactButton){
            waitAndClick("//label[text()='Hide Expand/Compact Button']//preceding-sibling::button");
        }

    }


    public void readToolTipData() throws InterruptedException {
        List<String> times = new ArrayList<>();
        List<String> values = new ArrayList<>();
        syncUntil(5000);
        Locator noOfElements=page.locator("g.highcharts-markers.highcharts-series-0 path.highcharts-point");
        for(int i=0;i<noOfElements.count();i++){
            Locator firstPath =
                    page.locator("g.highcharts-markers.highcharts-series-0 path.highcharts-point").nth(i);
            BoundingBox box = firstPath.boundingBox();
            if (box != null) {
                page.mouse().move(box.x - 5, box.y - 5);
                page.mouse().move(box.x + box.width / 2, box.y + box.height / 2);
            }
            Locator tSpans=page.locator("//*[name()='g'][contains(@class,'highcharts-label') and contains(@class,'highcharts-tooltip')]//*[local-name()='text']//*[local-name()='tspan']");
            String key = tSpans.nth(0).textContent().trim();
            String value = tSpans.nth(2).textContent().trim();
            times.add(key);
            values.add(value);
        }

        System.out.println(times.size());
        System.out.println(values.size());
    }

    public void verifyChartData() throws InterruptedException, IOException {
        System.out.println(GetCharDataApi.getTimeSeriesDataAccordingToKPIS().containsAll(getChartData()));
    }

    public void saveTheWidget(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
    }

    public void deleteDashboard(String dashboardName) {
        clickAndFill("//input[@placeholder='Search...']",  dashboardName);
        waitAndClick("//button[@data-state='closed']//preceding-sibling::div//child::a[text()='"+dashboardName+"']");
        waitAndClick("//div[text()='Delete']");
    }



}
