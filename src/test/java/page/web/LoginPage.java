package page.web;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import base.web.BasePage;
import com.trident.playwright.utils.WaitUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LoginPage extends BasePage {

    public LoginPage(Page page) {
        super(page);
    }
    private static final Logger log =
            LogManager.getLogger(LoginPage.class);
    Locator userName=page.locator("#username");
    Locator password=page.locator("#password");
    Locator loginButton=page.locator("#kc-login");

    public void login(String user, String pass) throws InterruptedException {
        enterUserName(userName,user);
        enterPassword(password,pass);
        clickLoginButton();
    }

    public void enterUserName(Locator userName, String user) throws InterruptedException {
        log.info("Entering username");
        WaitUtils.waitForVisible(userName,20000);
        waitAndFill(page,userName, user,2000);
        waitAndClick(page,loginButton,2000);
    }

    public void enterPassword(Locator password, String pass) throws InterruptedException {
        log.info("Entering password");
        waitAndFill(page,password, pass,2000);
    }

    public void clickLoginButton() throws InterruptedException {
        log.info("Clicking on Login button");
        waitAndClick(page,loginButton,2000);

    }
    public String getTitle() {
        return page.title();
    }

}
