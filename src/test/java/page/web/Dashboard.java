package page.web;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import base.web.BasePage;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Allure;
import utils.WaitUtils;

import java.util.List;


public class Dashboard extends BasePage {

    PageComponent pageComponent;
    private final Locator visibilityPublic;
    private final Locator addDashboardButton;
    private final Locator dashboardTitle;
    private final Locator dashboardDescription;
    private final Locator visibility;
    private final Locator createDashboard;
    private final Locator searchDashboard;
    private final Locator deleteDashboardMenu;
    private final Locator deleteButton;


    public Dashboard(Page page, BrowserContext context) {
        super(page,context);

        this.visibilityPublic = page.locator("//div[@dir ='ltr']//following::*[text()='Public (Everyone)']");
        this.addDashboardButton = getByRoleButton("Add Dashboard", page);
        this.dashboardTitle = getByPlaceholder("Enter dashboard title", page);
        this.dashboardDescription = getByPlaceholder("Enter dashboard description", page);
        this.visibility = page.locator("//button[@role='combobox']");
        this.createDashboard = getByRoleButton("Create", page);
        this.searchDashboard = getByPlaceholder("Search...", page);
        this.deleteDashboardMenu = page.locator("(//button[@data-state='closed']//preceding-sibling::div//following::button)[6]");
        this.deleteButton = page.getByRole(AriaRole.MENU).getByRole(AriaRole.MENUITEM, new Locator.GetByRoleOptions().setName("Delete"));

    }

    public String createDashboard(String dashboardName, String description, String visibilityType){
        Allure.step("Creating Dashboard");
        addDashboardButton.click();
        dashboardTitle.fill(dashboardName);
        dashboardDescription.fill(description);
        if (visibilityType.equals("Public")) {
            WaitUtils.waitForVisible(visibility, 2000);
            waitAndClick(visibility);
            waitAndClick(page, visibilityPublic, 2000);
        }
        createDashboard.click();
        return getBySpanAndText(dashboardName,page).textContent();
    }

    public void searchDashboard(String dashboardName) {
        Allure.step("Searching Dashboard");
        searchDashboard.fill(dashboardName);
        getByText(dashboardName, page).click();
    }

    public void deleteDashboard(String dashboardName) {
        Allure.step("Deleting Dashboard");
        searchDashboard.fill(dashboardName);
        waitAndClick(deleteDashboardMenu);
        //getByRoleButton();
        //getByText("Delete");
        deleteButton.click(new Locator.ClickOptions().setForce(true));
    }


}



