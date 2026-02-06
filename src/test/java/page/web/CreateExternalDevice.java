package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CreateExternalDevice extends BasePage {

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

    public CreateExternalDevice(Page page, BrowserContext browserContext) {
        super(page);
        this.page=page;
        this.browserContext=browserContext;
        this.externalDevicesPage=page.locator("//span[text()='External Devices']");
        this.createNewExternalDeviceButton=getByRoleButton("CREATE NEW",page);
        this.deviceIdentifierField=page.locator("#deviceIdentifier");
        this.deviceNameField= page.locator("#deviceName");
        this.organizationField=getByText("Select",page).first();
        this.plantField=getByText("Select",page).nth(1);
        this.externalDeviceTypeField=page.getByRole(AriaRole.COMBOBOX).first();
        this.saveButton=getBySpanAndText("Save",page);
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
}
