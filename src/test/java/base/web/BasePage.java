package base.web;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.List;
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

    public void enterMeasureName(List<String> measureName){
        waitAndClick("//span[text()='Select measures']//parent::button");
        Locator search=page.getByPlaceholder("Search measures...");
        for(String measure:measureName){
            search.click();
            page.keyboard().press("Control+A");
            page.keyboard().press("Delete");
            search.fill(measure);
            waitAndClick("//span[text()='"+measure+"']");
        }
    }

    public void waitAndClick(String locater){
        page.click(locater);
    }

    public void clickAndFill(String locater, String value){
        page.fill(locater, value);
    }
}