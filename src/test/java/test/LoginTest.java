package test;


import org.testng.annotations.Test;
import page.LoginPage;
import base.BaseTest;
import org.testng.annotations.BeforeTest;

public class LoginTest extends BaseTest {

    @Test(alwaysRun = true)
    public void validLoginTest() throws InterruptedException {

        LoginPage login = new LoginPage(page);
        login.login("covacsis_admin@techprescient.com", "MqdYgv29wAq5nG8CZY58B");
        System.out.println(page.url());
        System.out.println(page.title());
    }

}
