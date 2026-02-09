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
    private final Locator globalParameterNameL;
    private final Locator globalParameterDataType;
    private final Locator globalParameterDataTypeValueDouble;
    private final Locator globalParameterDimension;
    private final Locator globalParameterSave;
    private final Locator globalParameterDataTypeValueInt;


    public CreateGlobalParameter(Page childPage, BrowserContext context) {
        super(childPage,context);
        this.childPage=childPage;
        this.globalParameterList = getByText("Global Parameters List", childPage);
        this.createNewGlobalParameter = getByRoleButton("CREATE NEW", childPage);
        this.globalParameterNameL = getByPlaceholder("Global Parameter Name", childPage);
        this.globalParameterDataType = childPage.locator("//div[@class=' css-tlfecz-indicatorContainer']");
        this.globalParameterDataTypeValueDouble = childPage.locator("div[class*='menu']").getByText("Double", new Locator.GetByTextOptions().setExact(true));
        this.globalParameterDataTypeValueInt= childPage.locator("div[class*='menu']").getByText("Integer", new Locator.GetByTextOptions().setExact(true));
        this.globalParameterDimension = getByLabelCheckbox("Batch", childPage);
        this.globalParameterSave = getByRoleLabelText(childPage, "Save");
    }


    public void createGlobalParameter(String globalParameterName, String globalParameterType, String globalParameterDataType) {
        clickGlobalParameters();
        clickCreateNewParameter(globalParameterName,  globalParameterType, globalParameterDataType);
        clickSaveButton();
    }

    private void clickGlobalParameters() {
        globalParameterList.click();
    }

    private void clickCreateNewParameter(String gPName, String gpType, String gpDataType) {
        createNewGlobalParameter.click();
        globalParameterNameL.fill(gPName);
        globalParameterDataType.click();
        if (gpDataType.equals("Double")) {
            globalParameterDataTypeValueDouble.click();
        }
        else
            globalParameterDataTypeValueInt.click();
        if(gpType.equals("Batch"))
            globalParameterDimension.check();
    }

    private void clickSaveButton() {
        globalParameterSave.click();
    }
}
