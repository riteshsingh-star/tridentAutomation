package base.web;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import utils.ReadPropertiesFile;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import page.web.LoginPage;

import java.nio.file.Paths;
import java.util.List;


public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws InterruptedException {
        log.info("===== Test Setup Started =====");
        String browserName = ReadPropertiesFile.get("browser");
        boolean headless = Boolean.parseBoolean(ReadPropertiesFile.get("headless"));
        String baseUrl = ReadPropertiesFile.get("baseUrl");

        playwright = Playwright.create();

        BrowserType browserType;
        log.info("Browser: " + browserName);
        browserType = switch (browserName.toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> playwright.chromium();
        };
        log.info("Headless: " + headless);
        browser = browserType.launch(
                new BrowserType.LaunchOptions().setHeadless(headless).setArgs(List.of("--start-maximized"))
        );

        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        page = context.newPage();
        log.info("Navigating to URL: " + baseUrl);
        page.navigate(baseUrl);
        performLogin();
        //performSSOLogin();

    }


    private void performLogin(){
        String user = ReadPropertiesFile.get("webUserName");
        String pass = ReadPropertiesFile.get("webPassword");
        log.info("Starting login");
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(user, pass);
        log.info("Login successful");

    }

    private void performSSOLogin() {
        page.getByRole(AriaRole.TEXTBOX).fill("ostan.techlab@infinite-uptime.com");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter your email, phone, or Skype")).fill("ostan.techlab@infinite-uptime.com");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        page.locator("#i0118").fill("Sherlock01!");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
        page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Don't show this again")).check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
        page.waitForURL("**/dashboard**");
        context.storageState(new BrowserContext.StorageStateOptions().setPath(java.nio.file.Paths.get("auth/state.json")));

    }

    public Page getPage() {
        return page;
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
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
        log.info("===== Test Teardown Completed =====");
    }
}
