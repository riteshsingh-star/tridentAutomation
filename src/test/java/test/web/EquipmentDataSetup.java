package test.web;

import base.web.BaseTest;
import com.microsoft.playwright.Page;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import page.web.*;
import pojo.web.BaseData;
import utils.ReadJsonFile;

public class EquipmentDataSetup extends BaseTest {

    PageComponent pageComponent;
    Page childPage;
    CreateOrganization createOrganization;
    CreatePlant createPlant;
    CreateAreas createAreas;
    CreateEquipment createEquipment;
    BaseData data;


    @BeforeClass
    public void createAdminFlowSetupTest() {
        data = ReadJsonFile.readJson("baseDataCreation.json", BaseData.class);
        pageComponent = new PageComponent(page,context);
        childPage = pageComponent.moveToAdminPage(page, context);
        createOrganization = new CreateOrganization(childPage, context);
        createPlant = new CreatePlant(childPage, context);
        createAreas = new CreateAreas(childPage, context);
        createEquipment = new CreateEquipment(childPage, context);
    }

    @Test(priority = 0)
    public void createOrganizationTest() {
        createOrganization.createNewOrganization(data.organizationName(), data.language(), data.organizationType(), data.provider(), data.notes());
    }

    @Test(priority = 1)
    public void createPlant() {
        createPlant.createNewPlant(data.plantName(), data.organizationName(), data.regionName(), data.industryType(), data.language(), data.timeZone(), data.currency(), data.notes());
    }

    @Test(priority = 2)
    public void createArea() {
        createAreas.createArea(data.areaName(), data.plantName(), data.productionCapacity(), data.unit(), data.unitPrice(), data.unit(), data.directFaultValue(), data.environmentValue(), data.areaEnvironment(), data.temperature(), data.applicationType(), data.areaInchargeName(), data.areaInchargeNo(), data.areaInchargeEmail());
    }

    @Test(priority = 3)
    public void createEquipment() {
        createEquipment.createNewEquipment(data.equipmentName(), data.plantName(), data.areaName(), data.equipmentType(), data.makeManufacturer(), data.model(), data.serialNo(), data.appType());
    }

    @Test(priority = 4)
    public void createRawParameter() throws Exception {
        String rawParameterWay = data.rawParameterWay();
        createEquipment.openRawParameterPage(data.equipmentName());
        if (rawParameterWay.equals("One Raw Parameter")) {
            createEquipment.createRawParameter(data.rawParameterDataType(), data.rawParameterName(), data.rawParameterUnit(), data.LCLUCLType(), data.lclValue(), data.uclValue(), data.rawParameterNature(), data.rawParameterPrecision(), data.isExternal(), data.locationType(), data.nameOfTheLocation(), data.measureName());
        } else if (rawParameterWay.equals("Bulk Raw Parameter")) {
            createEquipment.createMultipleRawParameter(data.bulkRawParameter());
        }
    }
}
