package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.ViewportSize;
import com.microsoft.playwright.options.WaitForSelectorState;
import exception.NotAdminCredentials;
import io.qameta.allure.Allure;
import utils.ParseTheTimeFormat;
import utils.WaitUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageComponent extends BasePage {

    private final Locator chartGraph;
    private final Locator dataToolTip;
    private final Locator graphContainer;
    private final Locator reactBackground;
    private final Locator graphContainerPath;
    private final Locator searchMachine;
    private final Locator calender;
    private final Locator searchMeasure;
    private final Locator trendPanel;
    private final Locator quickLinks;
    private final Locator applyButton;
    private final Locator addParameter;
    private final Locator equipmentPage;
    private final Locator selectMeasure;
    private final Locator openPlantOS;
    private final Locator openAdminPage;
    private final Locator userType;

    //private final String isAdmin

    public PageComponent(Page page, BrowserContext context) {
        super(page, context);
        this.chartGraph = page.locator("//*[local-name()='g'][contains(@class,'highcharts-markers highcharts-series-0 highcharts-line-series highcharts-tracker')]//*[local-name()='path'][contains(@opacity,'1')]");
        this.dataToolTip = page.locator("//*[name()='g'][contains(@class,'highcharts-label') and contains(@class,'highcharts-tooltip')]//*[local-name()='text']//*[local-name()='tspan']");
        this.graphContainer = page.locator("div.highcharts-container");
        this.reactBackground = page.locator("//*[local-name()='rect' and contains(@class,'highcharts-plot-background')]");
        this.graphContainerPath = page.locator("//div[contains(@class,'highcharts-container')]");
        this.searchMachine = getByPlaceholder("Search...", page);
        this.calender = page.getByRole(AriaRole.BUTTON).getByText(Pattern.compile("\\d{2} .* AM|PM", Pattern.CASE_INSENSITIVE));
        this.searchMeasure = getByPlaceholder("Search measures...", page);
        this.trendPanel = byTitle("Show in trend panel", page);
        this.quickLinks = getByRoleButton("Quick Links", page);
        this.applyButton = getByRoleButton("Apply", page);
        this.addParameter = getByRoleButton("Add parameters", page);
        this.equipmentPage = getByRoleLink("Equipment", page);
        this.selectMeasure = page.locator("//span[text()='Select measures']//parent::button");
        this.openPlantOS = getByRoleButton("Open PlantOS App Suite", page);
        this.openAdminPage = page.locator("//span[text()='Admin Covacsis Service']");
        this.userType = page.locator("button[data-slot='dropdown-menu-trigger'] span");
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

    public Map<String, String> getChartData(int graphIndex) throws InterruptedException {
        /*page.waitForResponse(
                r -> r.url().contains("/api/kpis/timeseries") && r.status() == 200,
                () -> graphContainerPath.nth(graphIndex)
        );*/
        Allure.step("Fetching the Data from the Graph");
        Thread.sleep(20000);
        Locator chart = graphContainerPath.nth(graphIndex);
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
            if (tooltipSpans.count() > Math.max(0, 2)) {
                String key = tooltipSpans.nth(0).textContent().trim();
                String timeStamp=ParseTheTimeFormat.convertBrowserTimeToGMT(page,key);
                if (!key.equals(lastKey)) {
                    String value = tooltipSpans.nth(2).textContent().trim();
                    graphData.put(normalizeSpaces(timeStamp), ParseTheTimeFormat.formatStringTo2Decimal(value)
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
        getByRoleLink(equipmentName, page).click();
        calender.click();
        quickLinks.click();
        getByText(frequency, page).click();
        applyButton.click();
        addParameter.click();
        searchMeasure.fill(measureName);
        page.locator("//span[text()='" + measureName + "']").click();
        page.keyboard().press("Escape");
        trendPanel.click();
        Locator combo = page.getByRole(AriaRole.COMBOBOX);
        combo.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        combo.click();
        getByText(granularity, page).click();
    }

    public Page moveToAdminPage(Page page, BrowserContext context) {
        if (!isAdminUser()) {
            throw new NotAdminCredentials("Please login to Admin Credentials to perform this operation");
        } else {
            openPlantOS.click();
            Page newPage = context.waitForPage(() -> {
                openAdminPage.click();
            });
            newPage.waitForLoadState();
            return newPage;
        }
    }

    public Double getMeanAndSDFromUI(String type) {
        String sdText = page.getByText(Pattern.compile("^" + type + "\\s*=.*")).first().textContent().trim();
        return Double.parseDouble(sdText.replace(type + " =", "").trim());
    }


    public void dragTheChartGraph(Locator resizeHandle, Page page) {
        BoundingBox handleBox = resizeHandle.boundingBox();
        ViewportSize viewport = page.viewportSize();
        page.mouse().move(handleBox.x + handleBox.width / 2, handleBox.y + handleBox.height / 2);
        page.mouse().down();
        page.mouse().move(viewport.width - 20, viewport.height - 20, new Mouse.MoveOptions().setSteps(15));
        page.mouse().up();
    }

    public double getKpiValue(String kpiName, Page page) {
        Locator tile = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(kpiName));
        tile.waitFor();
        String rawText = tile.textContent().trim();
        Matcher m = Pattern.compile("(\\d+(?:,\\d+)*(?:\\.\\d+)?)").matcher(rawText);
        if (m.find()) {
            String number = m.group(1).replace(",", "");
            return Double.parseDouble(number);
        }
        throw new RuntimeException("No numeric value found in KPI tile: " + rawText);
    }

    private boolean isAdminUser() {
        boolean isAdminUser = false;
        String userText = userType.textContent().trim();
        System.out.println("userText: " + userText);
        if (userText.toLowerCase().contains("admin")) {
            System.out.println("Admin user logged in");
            isAdminUser = true;
        } else {
            System.out.println("Normal user logged in");
        }
        return isAdminUser;
    }
}
