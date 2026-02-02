package test;
import com.microsoft.playwright.Page;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import page.web.*;
import pojo.AdminFlow;
import utils.ReadJsonFile;
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
    public void createAdminFlowSetupTest(){
        data = ReadJsonFile.readJson("testData/adminFlow.json", AdminFlow.class);
        pageComponent=new PageComponent(page);
        childPage=pageComponent.moveToAdminPage(page,context);
        globalParameter=new CreateGlobalParameter(childPage,context);
        kpiDefinition = new CreateNewKPIDefinition(childPage, context);
        kpiAdvance = new KPIAdvanceImplementation(childPage,context);
    }

    @Test
    public void createGlobalParameterTest() {
        globalParameter.createGlobalParameter(data.globalParameterName());
    }

    @Test
    public void createNewKpiDefinitionTest() throws InterruptedException {
        kpiDefinition.createNewKPIDefinition(data.defineKPIName());
    }

    @Test
    public void validateGlobalParameterAndKPITest() {
        kpiAdvance.addLogicToTheKPIAndValidate(data.globalParameterName(), data.machineName(), data.formula(), data.defineKPIName(), data.date(), data.batchFrequency(), data.unit());
    }

}
