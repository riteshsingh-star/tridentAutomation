package base.web;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import factory.PlaywrightFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import utils.ReadPropertiesFile;
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
        log.info("===== Test Setup Started =====");
        String browserName = ReadPropertiesFile.get("browser");
        boolean headless = Boolean.parseBoolean(ReadPropertiesFile.get("headless"));
        String baseUrl = ReadPropertiesFile.get("webUrl");
        PlaywrightFactory.initBrowser(browserName, headless);
        PlaywrightFactory.createContextAndPage();
        page = PlaywrightFactory.getPage();
        context = PlaywrightFactory.getContext();
        page.navigate(baseUrl);
        performLogin();
    }

    private void performLogin() {
        String user = ReadPropertiesFile.get("userName");
        String pass = ReadPropertiesFile.get("webPassword");
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
        log.info("===== Test Teardown Started =====");
        PlaywrightFactory.closeContext();
        PlaywrightFactory.closeBrowser();
        log.info("===== Test Teardown Completed =====");
    }
}
