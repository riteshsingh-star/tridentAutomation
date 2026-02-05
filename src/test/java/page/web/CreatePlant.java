package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CreatePlant extends BasePage {

    Page page;
    BrowserContext browserContext;
    private final Locator plantPage;
    private final Locator createNewPlantButton;
    private final Locator plantNameField;
    private final Locator organizationField;
    private final Locator regionField;
    private final Locator industryField;
    private final Locator languageField;
    private final Locator timeZoneField;
    private final Locator currencyField;
    private final Locator notesField;
    private final Locator savePlantButton;

    public CreatePlant(Page page,BrowserContext browserContext){
        super(page);
        this.page=page;
        this.browserContext = browserContext;
        this.plantPage=getByText("Plants",page);
        this.createNewPlantButton=getByRoleButton("CREATE NEW",page);
        //this.plantNameField=page.getByRole(AriaRole.TEXTBOX,new Page.GetByRoleOptions().setName("plantName"));
        this.plantNameField=page.locator("#plantName");
        this.organizationField=page.getByRole(AriaRole.COMBOBOX).nth(0);
        this.regionField=page.getByRole(AriaRole.COMBOBOX).nth(1);
        this.industryField=page.getByRole(AriaRole.COMBOBOX).nth(2);
        this.languageField=page.getByRole(AriaRole.COMBOBOX).nth(3);
        this.timeZoneField=page.getByRole(AriaRole.COMBOBOX).nth(4);
        this.currencyField=page.getByRole(AriaRole.COMBOBOX).nth(5);
        this.notesField=page.getByRole(AriaRole.TEXTBOX).nth(4);
        this.savePlantButton=getByRoleButton("SAVE",page);
    }

    public void createNewPlant(String plantName, String organizationName, String regionName,String industryType, String language, String timeZone,String currency, String notes){
        plantPage.click();
        createNewPlantButton.click();
        plantNameField.fill(plantName);
        organizationField.selectOption(organizationName);
        regionField.selectOption(regionName);
        industryField.selectOption(industryType);
        languageField.selectOption(language);
        timeZoneField.selectOption(timeZone);
        currencyField.selectOption(currency);
        notesField.fill(notes);
    }

    public void savePlant(){
        savePlantButton.click();
    }
}
