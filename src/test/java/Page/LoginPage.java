package Page;

import com.microsoft.playwright.Page;
import com.trident.playwright.Base.BasePage;

public class LoginPage extends BasePage {

    private Page page;

    public LoginPage(Page page) {
        this.page = page;
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
