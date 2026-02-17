package factory;

import com.microsoft.playwright.*;

import java.awt.*;
import java.util.List;

public class WebFactory {

    protected static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    protected static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    protected static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    protected static ThreadLocal<Page> tlPage = new ThreadLocal<>();
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int width = (int) screenSize.getWidth();
    static int height = (int) screenSize.getHeight();

    public static void initBrowser(String browserName, boolean headless) {

        Playwright pw = Playwright.create();
        tlPlaywright.set(pw);
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);
        Browser browser = switch (browserName.toLowerCase()) {
            case "chrome" -> {
                options.setChannel("chrome");
                options.setArgs(List.of("--window-size=" + width + "," + height));
                yield pw.chromium().launch(options);
            }
            case "edge" -> {
                options.setChannel("msedge");
                options.setArgs(List.of("--window-size=" + width + "," + height));
                yield pw.chromium().launch(options);
            }
            case "firefox" -> pw.firefox().launch(options);
            case "safari", "webkit" -> pw.webkit().launch(options);
            default -> {
                options.setArgs(List.of("--window-size=" + width + "," + height));
                yield pw.chromium().launch(options);
            }
        };
        tlBrowser.set(browser);
    }

    public static void createContextAndPage() {
        BrowserContext context = tlBrowser.get().newContext(new Browser.NewContextOptions().setViewportSize(width, height));
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
