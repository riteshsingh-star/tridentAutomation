package Test;


import Page.LoginPage;
import com.trident.playwright.Base.BaseTest;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginTest() throws InterruptedException {

        LoginPage login = new LoginPage(page);
        login.login("covacsis_admin@techprescient.com", "MqdYgv29wAq5nG8CZY58B");
        System.out.println(page.url());
        System.out.println(page.title());
    }

}
