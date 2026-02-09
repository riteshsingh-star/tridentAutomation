package test.web;

import com.microsoft.playwright.options.LoadState;
import pojo.web.EquipmentMeasureValidation;
import utils.ReadJsonFile;
import base.web.BaseTest;
import org.testng.annotations.Test;
import page.web.EquipmentPageDataVerification;

public class DashboardVerify extends BaseTest {

    @Test
    public void dashboardVerify() throws InterruptedException {
        EquipmentMeasureValidation data = ReadJsonFile.readJson("testData/equipmentVerification.json", EquipmentMeasureValidation.class);
        EquipmentPageDataVerification equipmentVerificationPage= new EquipmentPageDataVerification(page,context);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        equipmentVerificationPage.verifyTheAggregateAccordingToFormula(data.equipmentName(),data.measureName(),data.frequency(),data.granularity(),data.aggregateType());
    }
}
