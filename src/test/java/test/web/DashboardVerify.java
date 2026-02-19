package test.web;

import com.microsoft.playwright.options.LoadState;
import pojo.web.EquipmentMeasureValidation;
import utils.ReadDataFile;
import base.web.BaseTest;
import org.testng.annotations.Test;
import page.web.EquipmentPageDataVerification;

public class DashboardVerify extends BaseTest {

    @Test
    public void dashboardVerify() throws Exception {
        EquipmentMeasureValidation data = ReadDataFile.loadDataFile(EquipmentMeasureValidation.class);
        EquipmentPageDataVerification equipmentVerificationPage= new EquipmentPageDataVerification(page,context);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        equipmentVerificationPage.verifyTheAggregateAccordingToFormula(data.equipmentName(),data.measureName(),data.frequency(),data.granularity(),data.aggregateType());
    }
}
