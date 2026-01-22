package page;

import com.microsoft.playwright.Page;
import base.BasePage;


public class Dashboard extends BasePage {


    public Dashboard(Page page) {
        super(page, context);
    }
    public void createDashboard(String dashboardName, String description) {
        page.click("//button[text()='Add Dashboard']");
        page.fill("input[id='title']", dashboardName);
        page.fill("input[id='description']", description);
        page.click("//button[@role='combobox']");
        //page.click("//div[@data-radix-popper-content-wrapper ='ltr']//following::*[text()='Public (Everyone)']");
        page.click("#radix-«r56»");
        //page.click("//button[text()='Create']");
    }

    public  void searchDashboard(String dashboardName) {
        page.fill("//input[@placeholder='Search...']",  dashboardName);
        page.click("//a[text()='"+dashboardName+"']");
    }

    public void deleteDashboard(String dashboardName) {
        page.fill("//input[@placeholder='Search...']",  dashboardName);
        page.click("//button[@data-state='closed']//preceding-sibling::div//child::a[text()='"+dashboardName+"']");
        page.click("//div[text()='Delete']");
    }



}
