package test;

import com.microsoft.playwright.options.LoadState;
import com.trident.playwright.pojo.DashboardData;
import com.trident.playwright.utils.ReadJsonFile;
import base.web.BaseTest;
import org.testng.annotations.Test;
import page.web.EquipmentPageDataVerification;



public class DashBoardVerify extends BaseTest {

    @Test
    public void dashboardverify() throws InterruptedException {
        DashboardData data =
                ReadJsonFile.readJson("testdata/dashboard.json", DashboardData.class);
        EquipmentPageDataVerification equipmentverificationpage= new EquipmentPageDataVerification(page);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        equipmentverificationpage.verifyTheAggregateAccordingToFormula();
    }
}
