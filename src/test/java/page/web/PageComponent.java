package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.ParseTheTimeFormat;
import utils.WaitUtils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PageComponent extends BasePage {

    private final Locator chartGraph;
    private final Locator dataToolTip;
    private final Locator graphContainer;
    private final Locator reactBackground;
    private final Locator graphContainerPath;
    private final Locator searchMachine;
    private final Locator calander;
    private final Locator searchMeasure;
    private final Locator trendPanel;
    private final Locator quickLinks;
    private final Locator applyButton;
    private final Locator addParameter;
    private final Locator equipmentPage;
    private final Locator selectMeasure;
    private final Locator openPlantOS;
    private final Locator openAdminPage;


    public PageComponent(Page page) {
        super(page);
        this.chartGraph = page.locator("//*[local-name()='g'][contains(@class,'highcharts-markers highcharts-series-0 highcharts-line-series highcharts-tracker')]//*[local-name()='path'][contains(@opacity,'1')]");
        this.dataToolTip = page.locator("//*[name()='g'][contains(@class,'highcharts-label') and contains(@class,'highcharts-tooltip')]//*[local-name()='text']//*[local-name()='tspan']");
        this.graphContainer = page.locator("div.highcharts-container");
        this.reactBackground = page.locator("//*[local-name()='rect' and contains(@class,'highcharts-plot-background')]");
        this.graphContainerPath = page.locator("//div[contains(@class,'highcharts-container')]");
        this.searchMachine=getByPlaceholder("Search...");
        this.calander=page.getByRole(AriaRole.BUTTON).getByText(Pattern.compile("\\d{2} .* AM|PM", Pattern.CASE_INSENSITIVE));
        this.searchMeasure=getByPlaceholder("Search measures...");
        this.trendPanel=byTitle("Show in trend panel");
        this.quickLinks=getByRoleButton("Quick Links");
        this.applyButton=getByRoleButton("Apply");
        this.addParameter=getByRoleButton("Add parameters");
        this.equipmentPage=getByRoleLink("Equipment");
        this.selectMeasure=page.locator("//span[text()='Select measures']//parent::button");
        this.openPlantOS=getByRoleButton("Open PlantOS App Suite");
        this.openAdminPage=getByRoleLink("Admin Service");

    }

    public Locator getChartContainer(int graphIndexToLocate) {
        WaitUtils.waitForVisible(graphContainer, 25000);
        graphContainer.nth(graphIndexToLocate);
        graphContainer.waitFor();
        return graphContainer;
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
      /* page.waitForResponse(
                r -> r.url().contains("/api/kpis/time series") && r.status() == 200,
                () -> graphContainerPath.nth(graphIndex)
        );*/
//        WaitUtils.waitForEnabled(page, page.locator("//*[local-name()='svg'][@width='826' and @height='180']"),25000);
        /*WaitUtils.waitForEnabled(page, page.locator("((//*[name()='svg']/*[name()='desc' and contains(text(), 'Highcharts')])["+graphIndex+"])/parent::*[name()='svg']"), 25000);
        page.locator("((//*[name()='svg']/*[name()='desc' and contains(text(), 'Highcharts')])["+graphIndex+"])/parent::*[name()='svg']").hover();
//        syncUntil(20000);*/
        syncUntil(20000);
        Locator chart =graphContainerPath.nth(graphIndex);
        chart.scrollIntoViewIfNeeded();
        Locator plotArea = chart.locator(reactBackground);
        activateChart(plotArea);
        Map<String, String> graphData = new LinkedHashMap<>();
        Locator tooltipSpans = dataToolTip;
        String lastKey = "";
        BoundingBox box = plotArea.boundingBox();
        if (box == null) return graphData;
        double y = box.y + box.height * 0.6;
        double startX = box.x - 10;
        double endX = box.x + box.width + 10;
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

    public void enterMeasureName(List<String> measureName) {
        waitAndClick(selectMeasure);
        for (String measure : measureName) {
            searchMeasure.click();
            page.keyboard().press("Control+A");
            page.keyboard().press("Delete");
            searchMeasure.fill(measure);
            page.locator("//span[text()='" + measure + "']").click();
        }
    }

    public void addMeasureToMachineInEquipmentPage(String equipmentName, String measureName, String frequency, String granularity) {
        equipmentPage.click();
        WaitUtils.waitForVisible(searchMachine, 4000);
        searchMachine.fill(equipmentName);
        getByRoleLink(equipmentName).click();
        calander.click();
        quickLinks.click();
        getByText(frequency).click();
        applyButton.click();
        addParameter.click();
        searchMeasure.fill(measureName);
       /* page.getByRole(AriaRole.CHECKBOX,
                new Page.GetByRoleOptions().setName(Pattern.compile("Availability", Pattern.CASE_INSENSITIVE))
        ).click();*/
        page.locator("//span[text()='" + measureName + "']").click();
        page.keyboard().press("Escape");
        trendPanel.click();
        Locator combo = page.getByRole(AriaRole.COMBOBOX);
        combo.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        combo.click();
        getByText(granularity).click();
    }

    public Page moveToAdminPage(Page page, BrowserContext context){
        openPlantOS.click();
        Page newPage = context.waitForPage(() -> {
            openAdminPage.click();
        });
        newPage.waitForLoadState();
        return newPage;
    }
}
