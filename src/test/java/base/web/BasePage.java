package base.web;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.WaitUtils;
import java.util.*;
import java.util.regex.Pattern;

public class BasePage {

    public Page page;
    public BasePage(Page page) {
        this.page = page;
    }

    public void syncUntil(long timeUnit) throws InterruptedException {
        Thread.sleep(timeUnit);
    }

    public void waitAndClick(Locator text) {
        text.click();
    }

    public static void waitAndClick(Page page, Locator locator, int waitMs) {
        page.waitForTimeout(waitMs);   // fixed wait
        locator.scrollIntoViewIfNeeded();
        locator.click();
    }

    public static void waitAndFill(Page page, Locator locator, String text, int waitMs) {
        page.waitForTimeout(waitMs);
        locator.scrollIntoViewIfNeeded();
        locator.fill(text);
    }

    public void waitForLocater(String locator){
        page.waitForSelector(locator);
    }

    public static String normalizeSpaces(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }

    public Locator getByRoleButton(String text, Page page) {
       return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(text));
    }

    public Locator getByPlaceholder(String placeholderValue,Page page) {
        return page.getByPlaceholder(placeholderValue);
    }

    public Locator getByText(String text,Page page) {
        Locator loc = page.getByText(text, new Page.GetByTextOptions().setExact(true));
        WaitUtils.waitForVisible(loc, 8000);
        return loc;
    }

    public void getByTextWithButtonParent(String text,Page page) {
        Locator buttonText = page.getByText(text).filter(new Locator.FilterOptions().setHas(page.locator("button")));
        WaitUtils.waitForVisible(buttonText, 4000);
        buttonText.click();
    }

    public Locator getByLabel(String text,Page page) {
       return page.getByLabel(text);
    }

    public Locator getByRoleOption(String text,Page page) {
        return page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(text).setExact(true));
    }

    public Locator getByLabelAndText(String label,Page page) {
       return page.getByLabel(label).getByText(label);
    }

    public Locator getByLabelButton(String LabelName,Page page) {
      return page.getByText(LabelName).locator("..").getByRole(AriaRole.COMBOBOX);
    }

    public Locator getByLabelButtonSwitch(String LabelName,Page page) {
        return page.getByText(LabelName).locator("..").getByRole(AriaRole.SWITCH);
    }

    public Locator getByRoleLink(String text,Page page) {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(text).setExact(true));
    }

    public Locator getByRoleTextbox(Page page) {
        return page.getByRole(AriaRole.TEXTBOX);
    }

    public Locator getByRoleLabelText(Page page,String text) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(text));
    }

    public Double getMeanAndSDFromUI(String type) {
        String sdText = page.getByText(Pattern.compile("^" + type + "\\s*=.*")).first().textContent().trim();
        return Double.parseDouble(sdText.replace(type + " =", "").trim());
    }

    public Locator byTitle(String text,Page page) {
        return page.getByTitle(text);
    }

    public Locator getByLabelCheckbox(String text,Page page) {
        return page.getByLabel(text, new Page.GetByLabelOptions().setExact(true));
    }

}