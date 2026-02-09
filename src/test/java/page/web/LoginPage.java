package page.web;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import base.web.BasePage;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.WaitUtils;

public class LoginPage extends BasePage {

    Locator userName;
    Locator password;
    Locator loginButton;

    public LoginPage(Page page, BrowserContext browserContext) {
        super(page,browserContext);
        this.userName = getByRoleTextbox(page);
        this.password = getByRoleTextbox(page);
        this.loginButton = getByRoleLabelText(page,"Login");
    }

    private static final Logger log = LogManager.getLogger(LoginPage.class);


    public void login(String user, String pass){
        enterUserName(userName, user);
        enterPassword(password, pass);
        clickLoginButton();
    }

    public void enterUserName(Locator userName, String user) {
        log.info("Entering username");
        Allure.step("Enter username "+user);
        WaitUtils.waitForVisible(userName, 20000);
        waitAndFill(page, userName, user, 2000);
        Allure.step("Clicking on Login Button");
        waitAndClick(page, loginButton, 2000);
    }

    public void enterPassword(Locator password, String pass){
        log.info("Entering password");
        Allure.step("Enter password ");
        waitAndFill(page, password, pass, 2000);
    }

    public void clickLoginButton(){
        log.info("Clicking on Login button");
        Allure.step("Clicking on Login button");
        waitAndClick(page, loginButton, 2000);

    }

    public String getTitle() {
        return page.title();
    }

}
