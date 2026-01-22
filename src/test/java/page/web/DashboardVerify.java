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
    public void EquipmentPage(int hour , int minutes ,String local) throws InterruptedException {

        page.getByRole(AriaRole.LINK,
                        new Page.GetByRoleOptions().setName("Equipment"))
                .click();
        page.click("(//div[@class='flex items-center gap-3'])[1]");
        page.click("//button[normalize-space()='Custom Time']");
        page.click("(//button[contains(text(),'AM') or contains(text(),'PM')])[1]");
        page.click("(//button[contains(text(),20)])[3]");
        page.click("((//div[@class='flex sm:flex-col p-2'])[1]//child::button)["+hour+"]");
        page.click("((//div[@class='flex sm:flex-col p-2'])[2]//child::button)["+minutes+"]");
        page.click("((//div[@class='flex sm:flex-col p-2'])[3]//child::button)["+local+"]");
        page.locator("div.p-5.overflow-y-auto").click();
        page.click("(//button[contains(text(),'AM') or contains(text(),'PM')])[2]");
        page.click("(//button[contains(text(),21)])[3]");
        //page.locator("div.p-5.overflow-y-auto").click();
        page.click("//button[@aria-label='Wednesday, January 21st, 2026, selected']");
        page.click("//button[normalize-space()='Apply']");




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



