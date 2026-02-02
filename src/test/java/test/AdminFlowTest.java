package test;

import io.qameta.allure.Step;
import pojo.AdminFlow;
import utils.ReadJsonFile;
import page.web.CreateAdminFlow;
import page.web.LoginPage;
import base.web.BaseTest;
import org.testng.annotations.Test;

public class AdminFlowTest extends BaseTest {
    //@Step ("Creating Global Parameter and KPI Definition and Validate the KPI in Advance Implementation")
    @Test
    public void createAdminFowTest() throws InterruptedException {
        CreateAdminFlow adminFlow=new CreateAdminFlow(page, context);
        AdminFlow data =
                ReadJsonFile.readJson("testData/adminFlow.json", AdminFlow.class);
        adminFlow.createGlobalParameter(data.parameterName());
        //adminFlow.createNewKPIDefinition(data.DefineKPIName());
        //adminFlow.addLogicToTheKPIAndValidate(data.editGlobalName(),data.editGlobalText(),data.SearchName());

    }
}
