package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CreateOrganization extends BasePage {
    Page page;
    BrowserContext browserContext;
    private final Locator organizationPage;
    private final Locator createNewOrganizationButton;
    private final Locator organizationName;
    private final Locator organizationLanguage;
    private final Locator organizationType;
    private final Locator organizationProvider;
    private final Locator notes;
    private final Locator saveOrganizationButton;


    public CreateOrganization(Page page, BrowserContext context) {
        super(page,context);
        this.page = page;
        this.browserContext = context;
        //this.organizationPage=getByText("Organizations",page).nth(1);
        this.organizationPage=page.locator("//span[text()='Organizations']");
        this.createNewOrganizationButton=getByRoleButton("CREATE NEW",page);
        //this.organizationName=page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Organization"));
        this.organizationName=page.locator("#organisationName");
        //this.organizationLanguage=getByLabel("Language",page);
        this.organizationLanguage=page.locator("#language-native-simple");
        //this.organizationType=getByLabel("Organization Type",page);
        //this.organizationProvider=getByLabel("Solution Provider",page);
        //this.notes=getByLabel("Notes",page);
        this.organizationType=page.locator("#organizationType");
        this.organizationProvider=page.locator("select[name='purveyourName']");
        this.notes=page.locator("#note");
        this.saveOrganizationButton=getBySpanAndText("Save",page);
    }

    public void createNewOrganization(String organizationNameValue,String organizationLanguageValue, String organizationTypeValue, String provider,String notesValue){
        organizationPage.nth(1).click();
        createNewOrganizationButton.click();
        organizationName.fill(organizationNameValue);
        organizationLanguage.selectOption(organizationLanguageValue);
        organizationType.selectOption(organizationTypeValue);
        organizationProvider.selectOption(provider);
        notes.fill(notesValue);
    }

    public void saveOrganization() {
        saveOrganizationButton.click();
    }
}
