package test.web;

import com.microsoft.playwright.Page;
import exception.NotAdminCredentials;
import org.testng.annotations.BeforeClass;
import page.web.*;
import pojo.web.AdminFlow;
import utils.ReadDataFile;
import base.web.BaseTest;
import org.testng.annotations.Test;

public class AdminFlowTest extends BaseTest {

    CreateGlobalParameter globalParameter;
    Page childPage;
    PageComponent pageComponent;
    AdminFlow data;
    CreateNewKPIDefinition kpiDefinition;
    KPIAdvanceImplementation kpiAdvance;

    @BeforeClass
    public void createAdminFlowSetupTest() throws Exception {
        data = ReadDataFile.loadDataFile(AdminFlow.class);
        pageComponent = new PageComponent(page, context);
        childPage = pageComponent.moveToAdminPage(page, context);
        if(childPage==null){
            throw new NotAdminCredentials("You don't have admin rights to perform this operation");
        }
        globalParameter = new CreateGlobalParameter(childPage, context);
        kpiDefinition = new CreateNewKPIDefinition(childPage, context);
        kpiAdvance = new KPIAdvanceImplementation(childPage, context);
    }

    @Test(priority = 0)
    public void createGlobalParameterTest() {
        globalParameter.createGlobalParameter(data.globalParameterName(), data.globalParameterType(), data.globalParameterDataType());
    }

    @Test(priority = 1)
    public void createNewKpiDefinitionTest() throws InterruptedException {
        kpiDefinition.createNewKPIDefinition(data.defineKPIName(), data.plantName(), data.globalParameterName(), data.aggregateType(), data.KPIPerformanceCriteria(), data.kpiUnits(), data.kpiPrecision(), data.industryType(), data.kpiType());
        //Assert.assertEquals(message,"Validation successful !!");
    }

    @Test(priority = 2)
    public void validateGlobalParameterAndKPITest() {
        kpiAdvance.addLogicToTheKPIAndValidate(data.globalParameterName(), data.defineKPIName(), data.machineName(), data.date(), data.batchFrequency(), data.unit(), data.formula());
    }

}
