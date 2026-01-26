package base.web;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;
import com.trident.playwright.utils.ParseTheTimeFormat;
import com.trident.playwright.utils.WaitUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

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
        page.waitForSelector(chartGraph, new Page.WaitForSelectorOptions().setTimeout(25000));
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

    public void getByRoleButton(String text){
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName(text)).click();
    }

    public void getByPlaceholder(String text){
        page.getByPlaceholder(text).click();
    }

    public void getByPlaceholder(String placeholderValue, String text ){
        page.getByPlaceholder(placeholderValue).fill(text);
    }

    public void getByText(String text){
       Locator loc= page.getByText(text);
        WaitUtils.waitForVisible(loc,8000);
        loc.click();
    }

    public void getByRoleButton(){
        page.getByRole(AriaRole.BUTTON).click();
    }

    public void getByTextWithButtonParent(String text){
       Locator buttonText= page.getByText(text).filter(new Locator.FilterOptions()
                .setHas(page.locator("button")));
       WaitUtils.waitForVisible(buttonText,4000);
       buttonText.click();
    }

    public void getByLabel(String text, String value){
        page.getByLabel(text).fill(value);
    }

    public void getByRoleWithPartialText(String text){
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(Pattern.compile(text, Pattern.CASE_INSENSITIVE))).click();
    }

    public void getByRoleOption(String text){
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(text)).click();
    }


    public void getByLabelAndText(String label){
        page.getByLabel(label).getByText(label).click();
    }

    public void getByLabelButton(String LabelName){
        page.getByText(LabelName).locator("..").getByRole(AriaRole.COMBOBOX).click();
    }

    public void getByLabelButtonSwitch(String LabelName){
        page.getByText(LabelName).locator("..").getByRole(AriaRole.SWITCH).click();
    }

    public void getByRoleLink(String text){
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(text)).click();
    }
}