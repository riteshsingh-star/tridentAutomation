package page.web;

import base.web.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.trident.playwright.utils.WaitUtils;
import org.testng.Assert;

import java.util.Map;
import java.util.regex.Pattern;

public class EquipmentPageDataVerification extends BasePage {

    public EquipmentPageDataVerification(Page page) {
        super(page);
    }

    public Map<String, String> openEquipmentAndAddKPI(String equipmentName, String measureName, String frequency, String granularity) throws InterruptedException {
        addMeasureToMachineInEquipmentPage(equipmentName, measureName, frequency, granularity);
        return getChartData(0,2,1);
    }

    public void verifyTheAggregateAccordingToFormula() throws InterruptedException {
        try {
            Map<String,String> tooltipData = openEquipmentAndAddKPI("SINGEING","Total_Water_Consumption","Yesterday","Eight Hour");
            System.out.println(tooltipData.size());
            System.out.println("Data"+ tooltipData);
            double chartAggregate=verifyTheAggregateData("Sum", tooltipData);
            double aggregateValue=getKpiValue("Total_Water_Consumption");
            Assert.assertEquals((int) chartAggregate, (int) aggregateValue, "Mismatch in integer part of aggregate value");
        }catch(InterruptedException e){
            System.out.println("Graph Aggregate is not Matching");
        }
    }
}
