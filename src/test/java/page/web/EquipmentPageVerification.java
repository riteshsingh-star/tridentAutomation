package page.web;

import base.web.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;

import java.util.Map;
import java.util.regex.Pattern;

public class EquipmentPageVerification extends BasePage {

    public EquipmentPageVerification(Page page) {
        super(page);
    }

    public Map<String, String> openEquipmentAndAddKPI(String equipmentName, String measureName, String frequency) throws InterruptedException {
        getByRoleLink("Equipment");
        getByPlaceholder("Search...", equipmentName);
        getByRoleLink(equipmentName);
        page.getByRole(AriaRole.BUTTON)
                .getByText(Pattern.compile("\\d{2} .* AM|PM", Pattern.CASE_INSENSITIVE)).click();
        getByRoleButton("Quick Links");
        getByText(frequency);
        getByRoleButton("Apply");
        getByRoleButton("Add parameters");
        //page.locator("section").filter(new Locator.FilterOptions().setHasText(equipmentName+"RunningEquipment")).getByLabel("Add parameters").click();
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
        getByText("Eight Hour");
        return getChartData(0,2,1);
    }

    public void verifyTheAggregateAccordingToFormula() throws InterruptedException {
        try {
            double chartAggregate=verifyTheAggregateData("Sum", openEquipmentAndAddKPI("SINGEING","Total_Water_Consumption","Last week"));
            double aggregateValue=getKpiValue("Total_Water_Consumption");
            Assert.assertEquals(chartAggregate,aggregateValue);
        }catch(InterruptedException e){
            System.out.println("Graph Aggregate is not Matching");
        }

    }
}
