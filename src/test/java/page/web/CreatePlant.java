package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CreatePlant extends BasePage {

    Page page;
    BrowserContext browserContext;
    private static final String plantPage = "Plants";
    private static final String createNewPlantButton = "CREATE NEW";
    private static final String plantNameField = "#plantName";
    private static final int organizationField = 0;
    private static final int regionField = 1;
    private static final int industryField = 2;
    private static final int languageField = 3;
    private static final int timeZoneField = 4;
    private static final int currencyField = 5;
    private static final int notesField = 4;
    private static final String savePlantButton = "SAVE";

    public CreatePlant(Page page, BrowserContext browserContext) {
        super(page, browserContext);
        this.page = page;
        this.browserContext = browserContext;
    }

    private Locator getComboBox(int index) {
        return getByRoleCombobox(page).nth(index);
    }

    private Locator getByTextbox(int index) {
        return getByRoleTextbox(page).nth(index);
    }

    private Locator getByText(String text) {
        return getByText(text, page);
    }

    private Locator getByRole(String text) {
        return getByRoleButton(text, page);
    }

    private Locator getByCSSXpath(String cssXpath) {
        return page.locator(cssXpath);
    }

    public void createNewPlant(String plantName, String organizationName, String regionName, String industryType, String language, String timeZone, String currency, String notes) {
        getByText(plantPage).click();
        getByRole(createNewPlantButton).click();
        getByCSSXpath(plantNameField).fill(plantName);
        getComboBox(organizationField).selectOption(organizationName);
        getComboBox(regionField).selectOption(regionName);
        getComboBox(industryField).selectOption(industryType);
        getComboBox(languageField).selectOption(language);
        getComboBox(timeZoneField).selectOption(timeZone);
        getByTextbox(notesField).fill(notes);
    }

    public void savePlant() {
        getByRole(savePlantButton).click();
    }
}
