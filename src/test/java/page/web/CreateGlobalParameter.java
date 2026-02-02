package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;


public class CreateGlobalParameter extends BasePage {

    BrowserContext context;
    Page childPage;
    PageComponent pageComponent;
    private final Locator globalParameterList;
    private final Locator createNewGlobalParameter;
    private final Locator globalParameterName;
    private final Locator globalParameterDataType;
    private final Locator globalParameterDataTypeValueDouble;
    private final Locator globalParameterDimension;
    private final Locator globalParameterSave;


    public CreateGlobalParameter(Page childPage, BrowserContext context) {
        super(childPage);
        this.context=context;
        this.childPage=childPage;
        this.globalParameterList = getByText("Global Parameters List", childPage);
        this.createNewGlobalParameter = getByRoleButton("CREATE NEW", childPage);
        this.globalParameterName = getByPlaceholder("Global Parameter Name", childPage);
        this.globalParameterDataType = childPage.locator("//div[@class=' css-tlfecz-indicatorContainer']");
        this.globalParameterDataTypeValueDouble = childPage.locator("div[class*='menu']").getByText("Double", new Locator.GetByTextOptions().setExact(true));
        this.globalParameterDimension = getByLabelCheckbox("Batch", childPage);
        this.globalParameterSave = getByRoleLabelText(childPage, "Save");
    }


    public void createGlobalParameter(String name) {
        clickGlobalParameters();
        clickCreateNewParameter(name);
        clickSaveButton();
    }

    private void clickGlobalParameters() {
        globalParameterList.click();
    }

    private void clickCreateNewParameter(String name) {

        createNewGlobalParameter.click();
        globalParameterName.fill(name);
        globalParameterDataType.click();
        globalParameterDataTypeValueDouble.click();
        globalParameterDimension.check();
    }

    private void clickSaveButton() {
        globalParameterSave.click();
    }
}
