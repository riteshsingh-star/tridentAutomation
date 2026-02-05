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

            //context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
            context=browser.newContext(
                    new Browser.NewContextOptions()
                            .setStorageStatePath(Paths.get("auth/state.json")).setViewportSize(null));
            page = context.newPage();
            log.info("Navigating to URL: " + baseUrl);
            page.navigate(baseUrl);
            //performLogin();

    }

//    @BeforeClass
//    public void ssoLogin() {
//        try (Playwright playwright = Playwright.create()) {
//            Browser browser = playwright.chromium().launch(
//                    new BrowserType.LaunchOptions().setHeadless(false)
//            );
//
//            BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
//            page = context.newPage();
//
//            page.navigate(
//                    "https://sandbox-new-identity.infinite-uptime.com/realms/idap/protocol/openid-connect/auth" +
//                            "?client_id=process-dashboard" +
//                            "&redirect_uri=https%3A%2F%2Fuat-new-process.infinite-uptime.com" +
//                            "&response_type=code" +
//                            "&scope=openid+profile+email" +
//                            "&state=6adb98a1877b4ba792c9ad8858da65d7" +
//                            "&code_challenge=M51-kZn1r29E-LIZ-BORp-dcIqxirx1wZiZtB2MSevE" +
//                            "&code_challenge_method=S256"
//            );
//
//            page.getByRole(AriaRole.TEXTBOX).fill("ostan.techlab@infinite-uptime.com");
//
//            page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Login")).click();
//
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter your email, phone, or Skype")).fill("ostan.techlab@infinite-uptime.com");
//
//            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
//
//            page.locator("#i0118").fill("Sherlock01!");
//
//            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
//
//            page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Don't show this again")).check();
//
//            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
//
//            page.waitForURL("**/dashboard**");
//
//            context.storageState(new BrowserContext.StorageStateOptions().setPath(java.nio.file.Paths.get("auth/state.json")));
//
//        }
//    }

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
