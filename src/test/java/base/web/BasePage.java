package base.web;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.WaitUtils;
import java.util.*;
import java.util.regex.Pattern;

public class BasePage {

    public static Page page;
    public BrowserContext context;
    private static final int timeout = 8000;

    public BasePage(Page page, BrowserContext context) {
        BasePage.page = page;
        this.context = context;
    }


    public static void waitAndClick(Locator text) {
        text.click();
    }

    public static void waitAndClick(Page page, Locator locator, int waitMs) {
        page.waitForTimeout(waitMs);
        locator.scrollIntoViewIfNeeded();
        locator.click();
    }

    public static void waitAndFill(Page page, Locator locator, String text, int waitMs) {
        page.waitForTimeout(waitMs);
        locator.scrollIntoViewIfNeeded();
        locator.fill(text);
    }

    public static void waitForLocater(String locator) {
        page.waitForSelector(locator);
    }

    public static String normalizeSpaces(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }

    public static Locator getByRoleButton(String text, Page page) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(text));
    }

    public static Locator getByPlaceholder(String placeholderValue, Page page) {
        return page.getByPlaceholder(placeholderValue);
    }

    public static Locator getByText(String text, Page page) {
        Locator loc = page.getByText(text, new Page.GetByTextOptions().setExact(true));
        WaitUtils.waitForVisible(loc, timeout);
        return loc;
    }

    public static void getByTextWithButtonParent(String text, Page page) {
        Locator buttonText = page.getByText(text).filter(new Locator.FilterOptions().setHas(page.locator("button")));
        WaitUtils.waitForVisible(buttonText, timeout);
        buttonText.click();
    }

    public static Locator getByLabel(String text, Page page) {
        return page.getByLabel(text);
    }

    public static Locator getByRoleOption(String text, Page page) {
        return page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(text).setExact(true));
    }

    public static Locator getByLabelAndText(String label, Page page) {
        return page.getByLabel(label).getByText(label);
    }

    public static Locator getByLabelButton(String LabelName, Page page) {
        return page.getByText(LabelName).locator("..").getByRole(AriaRole.COMBOBOX);
    }

    public static Locator getByLabelButtonSwitch(String LabelName, Page page) {
        return page.getByText(LabelName).locator("..").getByRole(AriaRole.SWITCH);
    }

    public static Locator getByRoleLink(String text, Page page) {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(text).setExact(true));
    }

    public static Locator getByRoleTextbox(Page page) {
        return page.getByRole(AriaRole.TEXTBOX);
    }

    public static Locator getByRoleLabelText(Page page, String text) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(text));
    }

    public static Locator byTitle(String text, Page page) {
        return page.getByTitle(text);
    }

    public static Locator getByLabelCheckbox(String text, Page page) {
        return page.getByLabel(text, new Page.GetByLabelOptions().setExact(true));
    }

    public static Locator getByAltText(String text, Page page) {
        return page.getByAltText(text);
    }

    public static Locator getBySpanAndText(String text, Page page) {
        return page.locator("span").filter(new Locator.FilterOptions().setHasText(text));
    }

    public static Locator getBySpanWithExactText(String text, Page page) {
        return page.locator("span").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^" + Pattern.quote(text) + "$")));
    }
}