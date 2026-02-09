package factory;

import com.microsoft.playwright.*;

import java.util.List;

public class PlaywrightFactory {

    protected static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    protected static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    protected static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    protected static ThreadLocal<Page> tlPage = new ThreadLocal<>();

    public static void initBrowser(String browserName, boolean headless) {
        Playwright pw = Playwright.create();
        tlPlaywright.set(pw);
        BrowserType type = switch (browserName.toLowerCase()) {
            case "firefox" -> pw.firefox();
            case "webkit" -> pw.webkit();
            default -> pw.chromium();
        };
        tlBrowser.set(type.launch(new BrowserType.LaunchOptions().setHeadless(headless).setArgs(List.of("--start-maximized"))));
    }

    public static void createContextAndPage() {
        BrowserContext context = tlBrowser.get().newContext(new Browser.NewContextOptions().setViewportSize(null));
        tlContext.set(context);
        tlPage.set(context.newPage());
    }

    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }

    public static Browser getBrowser() {
        return tlBrowser.get();
    }

    public static BrowserContext getContext() {
        return tlContext.get();
    }

    public static Page getPage() {
        return tlPage.get();
    }

    public static void closeContext() {
        if (tlContext.get() != null)
            tlContext.get().close();
        tlContext.remove();
        tlPage.remove();
    }

    public static void closeBrowser() {
        if (tlBrowser.get() != null)
            tlBrowser.get().close();
        if (tlPlaywright.get() != null)
            tlPlaywright.get().close();
        tlBrowser.remove();
        tlPlaywright.remove();
    }


}
