package page;

import com.microsoft.playwright.Page;
import base.BasePage;

public class LoginPage extends BasePage {

    public LoginPage(Page page) {
        super(page, context);
    }

    public void login(String user, String pass) throws InterruptedException {
        page.fill("#username", user);
        page.click("#kc-login");
        page.fill("#password", pass);
        syncUntil(2000);
        page.click("#kc-login");
    }

    public String getTitle() {
        return page.title();
    }

}
