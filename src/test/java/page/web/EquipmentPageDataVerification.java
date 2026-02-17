package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import utils.KPISCalculationUtils;
import org.testng.Assert;
import java.util.Map;

public class EquipmentPageDataVerification extends BasePage {

    PageComponent pageComponent;
    public EquipmentPageDataVerification(Page page, BrowserContext context) {
        super(page,context);
        pageComponent =new PageComponent(page, context);
    }

    public Map<String, String> openEquipmentAndAddKPI(String equipmentName, String measureName, String frequency, String granularity) throws InterruptedException {
        pageComponent.addMeasureToMachineInEquipmentPage(equipmentName, measureName, frequency, granularity);
        return pageComponent.getChartData(0);
    }

    public void verifyTheAggregateAccordingToFormula(String equipmentName, String measureName, String frequency, String granularity, String aggregateType) throws InterruptedException {
            Map<String,String> tooltipData = openEquipmentAndAddKPI(equipmentName,measureName,frequency,granularity);
            System.out.println(tooltipData.size());
            System.out.println("Data"+ tooltipData);
            double chartAggregate= KPISCalculationUtils.verifyTheAggregateData(aggregateType, tooltipData);
            double aggregateValue= pageComponent.getKpiValue(measureName,page);
            Assert.assertEquals((int) chartAggregate, (int) aggregateValue, "Mismatch in integer part of aggregate value");
            System.out.println(pageComponent.getMeanAndSDFromUI("SD"));
            System.out.println(pageComponent.getMeanAndSDFromUI("Mean"));
    }
}
