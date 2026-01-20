package base;

import com.microsoft.playwright.Page;

public class BasePage {

   public Page page;

    public BasePage(Page page) {
        this.page = page;
    }
    public void syncUntil(long timeUnit) throws InterruptedException {
        Thread.sleep(timeUnit);
    }

    public void waitForExplicitCondition(long timeUnit) throws InterruptedException {

    }
}