package page.web;

import base.web.BasePage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class DashboardVerify extends BasePage {

    public DashboardVerify(Page page, BrowserContext context) {
        super(page,context);
    }

    public void EquipmentPage(String SearchName) throws InterruptedException {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Equipment")).click();
        page.click("(//button[@data-slot=\"popover-trigger\"])[1]");

        //CalenderUtil.selectDate(page,2026,"January","20");

        page.locator("//button[text()='Calendar']").click();
        Locator locators = page.locator("//*[@class='rdp-weeks']/tr/td/button");
        int count = locators.count();

        // System.out.println(locators.isVisible());

        for (int i = 0; i < count; i++) {
            if(locators.nth(i).textContent().equals("20")  ){
                locators.nth(i).click();
            }
        }
        page.locator("//button[text()='Apply']").click();;



        //page.click("//div[@class='shrink-0 flex items-center justify-between border-t border-border bg-background px-4 py-3']//button[2]");
//        page.getByPlaceholder("Search...")
//                .fill("singeing");
        //getByPlaceholder("Search...",SearchName);

        page.locator("section")
                .filter(new Locator.FilterOptions().setHasText("SINGEING"))
                .getByRole(AriaRole.BUTTON,
                        new Locator.GetByRoleOptions().setName("Add parameters"))
                .click();

        page.click("(//button[@value='on'])[6]");

        page.keyboard().press("Escape");

        page.click("button[title='Show in trend panel']");



    }

//   public void verifyChartData() throws InterruptedException, IOException {
//        System.out.println(getChartData());
//    }
}



