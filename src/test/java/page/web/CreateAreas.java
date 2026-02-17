package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.rmi.registry.LocateRegistry;

public class CreateAreas extends BasePage {

    Page page;
    BrowserContext browserContext;
    private final Locator areaPage;
    private final Locator createNewAreaButton;
    private final Locator areaNameField;
    private final Locator plantNameField;
    private final Locator productionCapacityField;
    private final Locator productionCapacityUnitField;
    private final Locator unitPriceField;
    private final Locator unitPriceUnitField;
    private final Locator directFault;
    private final Locator environment_Type;
    private final Locator area_environment;
    private final Locator temperature;
    private final Locator applicationType;
    private final Locator area_Incharge_Name;
    private final Locator area_Incharge_No;
    private final Locator area_Incharge_Email;

    public CreateAreas(Page page, BrowserContext context) {
        super(page,context);
        this.page = page;
        this.browserContext = context;
        this.areaPage=getByRoleLink("Areas",page);
        this.createNewAreaButton=getByRoleButton("CREATE NEW",page);
        this.areaNameField=page.locator("#name");
        this.plantNameField=page.getByRole(AriaRole.COMBOBOX).nth(0);
        this.productionCapacityField=page.locator("#production_capacity");
        this.productionCapacityUnitField=getByRoleButton("Select",page).first();
        this.unitPriceField=page.locator("#unitPrice");
        this.unitPriceUnitField=getByRoleButton("Select",page).nth(1);
        this.directFault=page.locator("#direct_fault");
        this.environment_Type=page.locator("#environment_type");
        this.area_environment=getByRoleButton("Select",page).last();
        this.temperature=page.locator("#temperature");
        this.applicationType=page.locator("#applicationType");
        this.area_Incharge_Name=page.locator("#area_incharge_name");
        this.area_Incharge_No=page.locator(".PhoneInputInput");
        this.area_Incharge_Email=page.locator("#area_incharge_email_id");
    }

    public void createArea(String areaName, String plantName, String productionCapacity,String productionCapacityUnit,String unitPrice, String unitPriceUnit, String directFaultValue, String envValue, String areaEnv, String temp, String appType, String areaInch, String areaInchNo, String areaInchEmailV){
        areaPage.click();
        createNewAreaButton.click();
        areaNameField.fill(areaName);
        plantNameField.selectOption(plantName);
        productionCapacityField.fill(productionCapacity);
        productionCapacityUnitField.click();
        getByRoleOption(productionCapacityUnit,page).click();
        unitPriceField.fill(unitPrice);
        //unitPriceUnitField.click();
        //getByRoleOption(unitPriceUnit,page).click();
        directFault.clear();
        directFault.fill(directFaultValue);
        area_Incharge_Name.fill(areaInch);
        area_Incharge_No.fill(areaInchNo);
        area_Incharge_Email.fill(areaInchEmailV);
        environment_Type.fill(envValue);
        area_environment.click();
        getByRoleOption(areaEnv,page).click();
        page.keyboard().press("Escape");
        temperature.fill(temp);
        applicationType.fill(appType);
    }

    public  void saveAreas() throws InterruptedException {
        areaPage.click();
        createNewAreaButton.click();

    }
}
