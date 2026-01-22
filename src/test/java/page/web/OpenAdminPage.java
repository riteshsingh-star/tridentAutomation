package page.web;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public class OpenAdminPage {

    public static Page moveToAdminPage(Page page, BrowserContext context){
        page.click("//img[@class='h-full']");
        Page newPage = context.waitForPage(() -> {
            page.click("//span[text()='Admin Service']//parent::a");
        });
        newPage.waitForLoadState();
        return newPage;
    }
}
