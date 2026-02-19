package base.web;

import com.microsoft.playwright.*;
import config.EnvironmentConfig;
import factory.WebFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import page.web.LoginPage;


public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    protected Page page;
    protected BrowserContext context;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        log.info("CLASS_SETUP_START | className={}", this.getClass().getSimpleName());
        String browserName = EnvironmentConfig.getBrowser();
        boolean headless = Boolean.parseBoolean(EnvironmentConfig.getBrowserExecutionMode());
        String baseUrl = EnvironmentConfig.getWebURL();
        WebFactory.initBrowser(browserName, headless);
        WebFactory.createContextAndPage();
        page = WebFactory.getPage();
        context = WebFactory.getContext();
        page.navigate(baseUrl);
        performLogin();
        log.info("CLASS_SETUP_COMPLETE | className={}", this.getClass().getSimpleName());
    }

    private void performLogin() {
        String user = EnvironmentConfig.getUserName();
        String pass = EnvironmentConfig.getPassword();
        log.info("Starting login");
        LoginPage loginPage = new LoginPage(page, context);
        loginPage.login(user, pass);
        log.info("Login successful");

    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            log.error("Test Failed: {}", result.getName());
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        log.info("CLASS_TEARDOWN_START | className={}", this.getClass().getSimpleName());
        WebFactory.closeContext();
        WebFactory.closeBrowser();
        log.info("CLASS_TEARDOWN_COMPLETE | className={}", this.getClass().getSimpleName());

    }
}
