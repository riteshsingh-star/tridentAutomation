package page.web;

import com.microsoft.playwright.Page;
import base.web.BasePage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LoginPage extends BasePage {

    public LoginPage(Page page) {
        super(page);
    }
    private static final Logger log =
            LogManager.getLogger(LoginPage.class);
    public void login(String user, String pass) throws InterruptedException {

        log.info("Entering username");
        clickAndFill("#username", user);
        waitAndClick("#kc-login");
        log.info("Entering Password");
        clickAndFill("#password", pass);
        syncUntil(2000);
        log.info("Clicking on Login button");
        waitAndClick("#kc-login");
    }

    public String getTitle() {
        return page.title();
    }

}
