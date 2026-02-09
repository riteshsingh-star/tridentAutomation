package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CreateDevice extends BasePage {

    Page page;
    BrowserContext browserContext;
    private final Locator externalDevicesPage;
    private final Locator createNewExternalDeviceButton;
    private final Locator deviceIdentifierField;
    private  final Locator deviceNameField;
    private final Locator organizationField;
    private final Locator plantField;
    private final Locator externalDeviceTypeField;
    private final Locator saveButton;
    private final Locator devicesPage;
    private final Locator registerNewDeviceButton;
    private final Locator deviceIdentifierInDevice;
    private final Locator customDeviceIdentifierField;
    private final Locator wifiETHMachIdField;
    private final Locator deviceTypeField;
    private final Locator plantFieldInDevice;

    public CreateDevice(Page page, BrowserContext browserContext) {
        super(page,browserContext);
        this.page=page;
        this.browserContext=browserContext;
        this.externalDevicesPage=page.locator("//span[text()='External Devices']");
        this.devicesPage=page.locator("//span[text()='Devices']");
        this.createNewExternalDeviceButton=getByRoleButton("CREATE NEW",page);
        this.deviceIdentifierField=page.locator("#deviceIdentifier");
        this.deviceNameField= page.locator("#deviceName");
        //this.organizationField=getByText("Select",page).first();
        //this.plantField=getByText("Select",page).nth(1);
        this.organizationField=page.locator("//div[text()='Select']").first();
        this.plantField=page.locator("//div[text()='Select']").last();
        this.externalDeviceTypeField=page.getByRole(AriaRole.COMBOBOX).first();
        this.saveButton=getBySpanAndText("Save",page);
        this.registerNewDeviceButton=getByRoleButton("REGISTER",page);
        this.deviceIdentifierInDevice=page.locator("#deviceIdentifierForDevice");
        this.customDeviceIdentifierField=page.locator("#clientCustomDeviceIdentifierForDevice");
        this.wifiETHMachIdField=page.locator("#wifiMacIdForDevice");
        this.deviceTypeField=page.getByRole(AriaRole.COMBOBOX).first();
        this.plantFieldInDevice=page.getByRole(AriaRole.COMBOBOX).nth(1);
    }

    public void createExternalDevice(String deviceIdentifier, String deviceName, String organizationName, String plantName, String externalDeviceType){
        externalDevicesPage.click();
        createNewExternalDeviceButton.click();
        deviceIdentifierField.fill(deviceIdentifier);
        deviceNameField.fill(deviceName);
        organizationField.click();
        getByText(organizationName,page).click();
        plantField.click();
        getByText(plantName,page).click();
        externalDeviceTypeField.selectOption(externalDeviceType);
        saveButton.click();
    }


    public void createDevice(String deviceName, String deviceType,String plantName){
        devicesPage.click();
        registerNewDeviceButton.click();
        deviceIdentifierInDevice.fill(deviceName);
        //customDeviceIdentifierField.fill(deviceName);
        //wifiETHMachIdField.fill(deviceName);
        deviceTypeField.selectOption(deviceType);
        plantFieldInDevice.selectOption(plantName);
        saveButton.click();
    }
}
