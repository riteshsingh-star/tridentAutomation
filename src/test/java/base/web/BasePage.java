package base.web;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.WaitUtils;
import java.util.*;
import java.util.regex.Pattern;

public class BasePage {

    public Page page;
    public BrowserContext context;
    private final int timeout = 8000;

    public BasePage(Page page, BrowserContext context) {
        this.page = page;
        this.context = context;
    }


    public void waitAndClick(Locator text) {
        text.click();
    }

    public void waitAndClick(Page page, Locator locator, int waitMs) {
        page.waitForTimeout(waitMs);
        locator.scrollIntoViewIfNeeded();
        locator.click();
    }

    public void waitAndFill(Page page, Locator locator, String text, int waitMs) {
        page.waitForTimeout(waitMs);
        locator.scrollIntoViewIfNeeded();
        locator.fill(text);
    }

    public void waitForLocater(String locator) {
        page.waitForSelector(locator);
    }

    public String normalizeSpaces(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }

    public Locator getByRoleButton(String text, Page page) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(text));
    }

    public Locator getByPlaceholder(String placeholderValue, Page page) {
        return page.getByPlaceholder(placeholderValue);
    }

    public Locator getByText(String text, Page page) {
        Locator loc = page.getByText(text, new Page.GetByTextOptions().setExact(true));
        WaitUtils.waitForVisible(loc, timeout);
        return loc;
    }

    public void getByTextWithButtonParent(String text, Page page) {
        Locator buttonText = page.getByText(text).filter(new Locator.FilterOptions().setHas(page.locator("button")));
        WaitUtils.waitForVisible(buttonText, timeout);
        buttonText.click();
    }

    public Locator getByLabel(String text, Page page) {
        return page.getByLabel(text);
    }

    public Locator getByRoleOption(String text, Page page) {
        return page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(text).setExact(true));
    }

    public Locator getByLabelAndText(String label, Page page) {
        return page.getByLabel(label).getByText(label);
    }

    public Locator getByLabelButton(String LabelName, Page page) {
        return page.getByText(LabelName).locator("..").getByRole(AriaRole.COMBOBOX);
    }

    public Locator getByLabelButtonSwitch(String LabelName, Page page) {
        return page.getByText(LabelName).locator("..").getByRole(AriaRole.SWITCH);
    }

    public Locator getByRoleLink(String text, Page page) {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(text).setExact(true));
    }

    public Locator getByRoleTextbox(Page page) {
        return page.getByRole(AriaRole.TEXTBOX);
    }

    public Locator getByRoleLabelText(Page page, String text) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(text));
    }

    public Locator byTitle(String text, Page page) {
        return page.getByTitle(text);
    }

    public Locator getByLabelCheckbox(String text, Page page) {
        return page.getByLabel(text, new Page.GetByLabelOptions().setExact(true));
    }

    public Locator getByAltText(String text, Page page) {
        return page.getByAltText(text);
    }

    public Locator getBySpanAndText(String text, Page page) {
        return page.locator("span").filter(new Locator.FilterOptions().setHasText(text));
    }

    public Locator getBySpanWithExactText(String text, Page page) {
        return page.locator("span").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^" + Pattern.quote(text) + "$")));
    }

    public Locator getByRoleCombobox(Page page) {
       return page.getByRole(AriaRole.COMBOBOX);
    }
    public void scrollTillVisible(Locator locator) {
        locator.scrollIntoViewIfNeeded();
    }

    protected boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    protected String getText(String selector) {
        return page.locator(selector).textContent();
    }



}