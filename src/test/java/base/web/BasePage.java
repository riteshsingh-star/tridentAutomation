package base.web;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.trident.playwright.utils.ParseTheTimeFormat;
import com.trident.playwright.utils.WaitUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasePage {

   public Page page;

   private static final String chartGraph="//*[local-name()='g'][contains(@class,'highcharts-markers highcharts-series-0 highcharts-line-series highcharts-tracker')]//*[local-name()='path'][contains(@opacity,'1')]";
   private static final String dataToolTip="//*[name()='g'][contains(@class,'highcharts-label') and contains(@class,'highcharts-tooltip')]//*[local-name()='text']//*[local-name()='tspan']";
   private static final String graphContainer="div.highcharts-container";
   private static final String reactBackground="//*[local-name()='rect' and contains(@class,'highcharts-plot-background')]";
   private static final String graphContainerPath="//div[contains(@class,'highcharts-container')]";

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


    public Locator getChartContainer(int graphIndexToLocate) {
        page.waitForSelector(graphContainer, new Page.WaitForSelectorOptions().setTimeout(25000));
        Locator chart = page.locator(graphContainer).nth(graphIndexToLocate);
        chart.waitFor();
        return chart;
    }

    public void activateChart(Locator plotArea) {
        plotArea.waitFor();
        BoundingBox box = plotArea.boundingBox();
        if (box == null)
            return;
        double x = box.x + box.width / 2;
        double y = box.y + box.height / 2;

        page.mouse().move(x, y);
        page.waitForTimeout(150);
    }

    public Map<String, String> getChartData(int timeStampIndex, int dataIndex, int graphIndex) throws InterruptedException {
        WaitUtils.waitForVisible(graphContainerPath,25000);
        Locator chart = page.locator(graphContainerPath).nth(graphIndex);
        Locator plotArea = chart.locator(reactBackground);
        activateChart(plotArea);
        Map<String, String> graphData = new LinkedHashMap<>();
        Locator tooltipSpans = page.locator(dataToolTip);
        String lastKey = "";
        BoundingBox box = plotArea.boundingBox();
        if (box == null) return graphData;
        double y = box.y + box.height * 0.6;
        double startX = box.x - 10;
        double endX   = box.x + box.width + 10;
        for (double x = startX; x <= endX; x += 4) {
            page.mouse().move(x, y);
            page.waitForTimeout(25);
            if (tooltipSpans.count() > Math.max(timeStampIndex, dataIndex)) {
                String key = tooltipSpans.nth(timeStampIndex).textContent().trim();
                if (!key.equals(lastKey)) {
                    String value = tooltipSpans.nth(dataIndex).textContent().trim();
                    graphData.put(normalizeSpaces(key), ParseTheTimeFormat.formatStringTo2Decimal(value)
                    );
                    lastKey = key;
                }
            }
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
       Locator loc= page.getByText(text, new Page.GetByTextOptions().setExact(true));
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
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(text).setExact(true)).click();
    }

    public Locator getByRoleTextbox(){
        return page.getByRole(AriaRole.TEXTBOX);
    }

    public Locator getByRoleLabelText(){
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
    }

    public Locator getByRoleButtonOptions(String text){
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(text));
    }

    public double verifyTheAggregateData(String aggregateType, Map<String, String> tooltipData) {
        double sum = 0.0;
        int count = 0;
        for (Map.Entry<String, String> entry : tooltipData.entrySet()) {

            String valueStr = entry.getValue();
            if (valueStr == null || valueStr.isEmpty())
                continue;
            valueStr = valueStr.replace(",", "").replace("%", "").replaceAll("[a-zA-Z]", "").trim();
            try {
                double val = Double.parseDouble(valueStr);
                sum += val;
                count++;
            } catch (NumberFormatException e) {
                System.out.println("Error in verifyTheAggregate: " + valueStr);
            }
        }
        if (aggregateType.equalsIgnoreCase("Sum")) {
            return sum;
        }
        if (aggregateType.equalsIgnoreCase("Average")) {
            return count == 0 ? 0.0 : sum / count;
        }

        throw new IllegalArgumentException("Invalid aggregateType: " + aggregateType);
    }


    public double getKpiValue(String kpiName) {
        Locator tile =
                page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName(kpiName));
        tile.waitFor();
        String rawText =
                tile.textContent().trim();
        Matcher m = Pattern.compile("(\\d+(?:,\\d+)*(?:\\.\\d+)?)").matcher(rawText);
        if (m.find()) {
            String number = m.group(1).replace(",", "");
            return Double.parseDouble(number);
        }
        throw new RuntimeException("No numeric value found in KPI tile: " + rawText);
    }

    public void addMeasureToMachineInEquipmentPage(String equipmentName, String measureName, String frequency, String granularity){
        getByRoleLink("Equipment");
        WaitUtils.waitForVisible(page.getByPlaceholder("Search..."),4000);
        getByPlaceholder("Search...", equipmentName);
        getByRoleLink(equipmentName);
        page.getByRole(AriaRole.BUTTON)
                .getByText(Pattern.compile("\\d{2} .* AM|PM", Pattern.CASE_INSENSITIVE)).click();
        getByRoleButton("Quick Links");
        getByText(frequency);
        getByRoleButton("Apply");
        getByRoleButton("Add parameters");
        getByPlaceholder("Search measures...", measureName);
       /* page.getByRole(AriaRole.CHECKBOX,
                new Page.GetByRoleOptions().setName(Pattern.compile("Availability", Pattern.CASE_INSENSITIVE))
        ).click();*/
        waitAndClick("//span[text()='"+measureName+"']");
        page.keyboard().press("Escape");
        page.getByTitle("Show in trend panel").click();
        Locator combo = page.getByRole(AriaRole.COMBOBOX);
        combo.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        combo.click();
        getByText(granularity);
    }
}