package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class DashboardVerify  extends BasePage {

    public DashboardVerify(Page page, BrowserContext context) {
        super(page);
    }
    public void EquipmentPage() throws InterruptedException {

        page.getByRole(AriaRole.LINK,
                        new Page.GetByRoleOptions().setName("Equipment"))
                .click();

        page.getByPlaceholder("Search...")
                .fill("singeing");

        page.locator("section")
                .filter(new Locator.FilterOptions().setHasText("SINGEING"))
                .getByRole(AriaRole.BUTTON,
                        new Locator.GetByRoleOptions().setName("Add parameters"))
                .click();

        page.click("(//button[@value='on'])[5]");

        page.keyboard().press("Escape");

        page.click("button[title='Show in trend panel']");
    }

}



