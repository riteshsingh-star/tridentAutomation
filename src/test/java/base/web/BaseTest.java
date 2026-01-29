package base.web;

import com.microsoft.playwright.*;
import utils.ReadPropertiesFile;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import page.web.LoginPage;
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

    }

    private void performLogin() throws InterruptedException {
            String user = ReadPropertiesFile.get("webUserName");
            String pass = ReadPropertiesFile.get("webPassword");
            log.info("Starting login");
            LoginPage loginPage = new LoginPage(page);
            loginPage.login(user, pass);
            log.info("Login successful");

    }

    public Page getPage() {
        return page;
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            log.error("Test Failed: "+ result.getName());
        }
        context.close();
        browser.close();
        playwright.close();
    }
}
