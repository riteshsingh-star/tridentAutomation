package base.web;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.trident.playwright.utils.ParseTheTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BasePage {

   public Page page;

   private static String chartGraph="//*[local-name()='g'][contains(@class,'highcharts-markers highcharts-series-0 highcharts-line-series highcharts-tracker')]//*[local-name()='path'][contains(@opacity,'1')]";
   private static String dataToolTip="//*[name()='g'][contains(@class,'highcharts-label') and contains(@class,'highcharts-tooltip')]//*[local-name()='text']//*[local-name()='tspan']";


    public BasePage(Page page) {
        this.page = page;
    }
    public void syncUntil(long timeUnit) throws InterruptedException {
        Thread.sleep(timeUnit);
    }

    public void enterMeasureName(List<String> measureName){
        waitAndClick("//span[text()='Select measures']//parent::button");
        Locator search=page.getByPlaceholder("Search measures...");
        for(String measure:measureName){
            search.click();
            page.keyboard().press("Control+A");
            page.keyboard().press("Delete");
            search.fill(measure);
            waitAndClick("//span[text()='"+measure+"']");
        }
    }

    public void waitAndClick(String text){
        page.click(text);
    }
    public static void waitAndClick(Page page, Locator locator, int waitMs) {
        page.waitForTimeout(waitMs);   // fixed wait
        locator.scrollIntoViewIfNeeded();
        locator.click();
    }

    public void clickAndFill(String locater, String text ){
        page.fill(locater,text);
    }
    public static void waitAndFill(Page page, Locator locator, String text, int waitMs) {
        page.waitForTimeout(waitMs);
        locator.scrollIntoViewIfNeeded();
        locator.fill(text);
    }

    public void waitForLocater(String locator){
        page.waitForSelector(locator);
    }

    public Map<String,String> getChartData(int timeStampIndex, int dataIndex) throws InterruptedException {
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
            String key = tSpans.nth(timeStampIndex).textContent().trim();
            String value = tSpans.nth(dataIndex).textContent().trim();
            graphData.put(normalizeSpaces(key), ParseTheTimeFormat.formatStringTo2Decimal(value));
        }
        return graphData;
    }

    public static String normalizeSpaces(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }

}