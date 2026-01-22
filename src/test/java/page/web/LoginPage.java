package page.web;

import com.microsoft.playwright.Page;
import base.web.BasePage;

public class LoginPage extends BasePage {

    public LoginPage(Page page) {
        super(page, context);
    }

    public void login(String user, String pass) throws InterruptedException {
        clickAndFill("#username", user);
        waitAndClick("#kc-login");
        clickAndFill("#password", pass);
        syncUntil(2000);
        waitAndClick("#kc-login");
    }

    public String getTitle() {
        return page.title();
    }

}
