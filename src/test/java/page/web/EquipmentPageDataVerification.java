package page.web;

import base.web.BasePage;
import com.microsoft.playwright.Page;
import utils.KPISCalculationUtils;
import org.testng.Assert;
import java.util.Map;

public class EquipmentPageDataVerification extends BasePage {

    PageComponent pageComponent;
    public EquipmentPageDataVerification(Page page) {
        super(page);
        pageComponent =new PageComponent(page);
    }

    public Map<String, String> openEquipmentAndAddKPI(String equipmentName, String measureName, String frequency, String granularity) throws InterruptedException {
        pageComponent.addMeasureToMachineInEquipmentPage(equipmentName, measureName, frequency, granularity);
        return pageComponent.getChartData(0,2,1);
    }

    public void verifyTheAggregateAccordingToFormula() throws InterruptedException {
        try {
            Map<String,String> tooltipData = openEquipmentAndAddKPI("SINGEING","Total_Water_Consumption","Last week","Eight Hour");
            System.out.println(tooltipData.size());
            System.out.println("Data"+ tooltipData);
            double chartAggregate= KPISCalculationUtils.verifyTheAggregateData("Sum", tooltipData);
            double aggregateValue= KPISCalculationUtils.getKpiValue("Total_Water_Consumption",page);
            Assert.assertEquals((int) chartAggregate, (int) aggregateValue, "Mismatch in integer part of aggregate value");
            System.out.println(getMeanAndSDFromUI("SD"));
            System.out.println(getMeanAndSDFromUI("Mean"));
        }catch(InterruptedException e){
            System.out.println("Graph Aggregate is not Matching");
        }
    }
}
