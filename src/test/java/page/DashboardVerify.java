package page;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import base.BasePage;

public class DashboardVerify extends BasePage {

    public DashboardVerify(Page page, BrowserContext context) {
        super(page, context);
    }

    public void EquipmentPage() throws InterruptedException {
        page.click("a[aria-label='Equipment']");
        syncUntil(2000);
        page.fill("input[placeholder='Search...']", "singeing");
        page.click("img[alt='add parameters']");
        page.click("(//button[@value='on'])[5]");
        syncUntil(1000);
        page.click("button[title='Show in trend panel']");
        page.click("button[title='Show in trend panel']");
    }
}
