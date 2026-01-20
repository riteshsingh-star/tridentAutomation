package base;

import com.microsoft.playwright.*;
import com.trident.playwright.utils.ReadPropertiesFile;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;


public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        String browserName = ReadPropertiesFile.get("browser");
        boolean headless = Boolean.parseBoolean(ReadPropertiesFile.get("headless"));
        String baseUrl = ReadPropertiesFile.get("baseUrl");

        playwright = Playwright.create();

        BrowserType browserType;

        switch (browserName.toLowerCase()) {
            case "firefox":
                browserType = playwright.firefox();
                break;
            case "webkit":
                browserType = playwright.webkit();
                break;
            default:
                browserType = playwright.chromium();
        }

        browser = browserType.launch(
                new BrowserType.LaunchOptions().setHeadless(headless)
        );

        context = browser.newContext();
        page = context.newPage();

        page.navigate(baseUrl);
    }

    @AfterMethod
    public void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}
