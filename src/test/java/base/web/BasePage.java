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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BasePage {

   public Page page;

    public BasePage(Page page) {
        this.page = page;
    }
    public void syncUntil(long timeUnit) throws InterruptedException {
        Thread.sleep(timeUnit);
    }

    public void waitForExplicitCondition(long timeUnit) throws InterruptedException {

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

    public List<String> getChartData() throws InterruptedException {
        List<String> times = new ArrayList<>();
        //List<String> values = new ArrayList<>();
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

            times.add(normalizeSpaces(key+ " "+ ParseTheTimeFormat.formatStringTo2Decimal(value)));
        }

        return times;
    }

    public static String normalizeSpaces(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }

}