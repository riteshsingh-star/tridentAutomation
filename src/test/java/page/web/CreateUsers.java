package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CreateUsers extends BasePage{

    Page page;
    BrowserContext browserContext;
    private final Locator userPage;
    private final Locator createNewUserButton;
    private final Locator emailIdField;
    private final Locator firstNameField;
    private final Locator lastNameField;
    private final Locator organizationField;
    private final Locator phoneNumberField;
    private final Locator languageField;
    private final Locator userRoleField;
    private final Locator unitSystemField;
    private final Locator timeZoneField;
    private final Locator alertMediaField;
    private final Locator alertTypeField;
    private final Locator adminNonAdminButton;
    private final Locator addPlantButton;



    public CreateUsers(Page page,  BrowserContext browserContext) {
        super(page);
        this.page = page;
        this.browserContext = browserContext;
        this.userPage=page.locator("//span[text()='Users']");
        this.createNewUserButton=getByRoleButton("CREATE NEW",page);
        this.emailIdField=page.locator("#emailLabelForUser");
        this.firstNameField=page.locator("#firstNameForUser");
        this.lastNameField=page.locator("#lastNameForUser");
        this.organizationField=page.getByRole(AriaRole.COMBOBOX).first();
        this.phoneNumberField=page.locator(".PhoneInputInput");
        this.languageField=page.getByRole(AriaRole.COMBOBOX).nth(1);
        this.userRoleField=page.getByRole(AriaRole.COMBOBOX).nth(2);
        this.unitSystemField=page.getByRole(AriaRole.COMBOBOX).nth(3);
        this.timeZoneField=page.getByRole(AriaRole.COMBOBOX).nth(4);
        this.alertMediaField=page.getByRole(AriaRole.COMBOBOX).nth(5);
        this.alertTypeField=page.locator("#select-entityType");
        this.adminNonAdminButton=getByLabel("myonoffswitchAdmin",page);
        this.addPlantButton=getByRoleButton("Add Plants",page);
    }


    public void createNewUser(String email, String firstName, String lastName, String organizationName, long phoneNumber, String language, String unit, String timeZone, String alertMedia, String alertType, boolean isAdmin){
        userPage.click();
        createNewUserButton.click();
        emailIdField.fill(email);
        firstNameField.fill(firstName);
        lastNameField.fill(lastName);
        organizationField.selectOption(organizationName);
        phoneNumberField.fill(String.valueOf(phoneNumber));
        languageField.selectOption(language);
        unitSystemField.selectOption(unit);
        timeZoneField.selectOption(timeZone);
        alertMediaField.selectOption(alertMedia);
        alertTypeField.click();
        getByRoleOption(alertType,page);
        if(isAdmin){
            adminNonAdminButton.click();
        }

    }


    private void selectAndAddPlant(String organizationName, String plantName){
        addPlantButton.click();
        getByRoleButton(organizationName,page);
        getBySpanAndText(plantName,page);
        getByRoleButton("UPDATE",page);
    }
}
