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

    public void waitAndClick(String locater){
        page.click(locater);
    }

    public void clickAndFill(String locater, String value){
        page.fill(locater, value);
    }

    public void waitForLocater(String locator){
        page.waitForSelector(locator);
    }

    public Set<String> getChartData() throws InterruptedException {
        Set<String> times = new LinkedHashSet<>();
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
            String key = tSpans.nth(0).textContent().trim();
            String value = tSpans.nth(3).textContent().trim();
            times.add(normalizeSpaces(key+ " "+ ParseTheTimeFormat.formatStringTo2Decimal(value)));
        }
        return times;
    }

    public static String normalizeSpaces(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }

}