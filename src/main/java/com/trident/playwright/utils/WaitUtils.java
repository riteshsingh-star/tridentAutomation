package com.trident.playwright.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class WaitUtils {

    public static void waitForAttached(Locator locator, int timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(timeoutMs));
    }

    public static void waitForVisible(Locator locator, int timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeoutMs));
    }

    public static void waitForHidden(Locator locator, int timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(timeoutMs));
    }

    public static void waitForEnabled(Page page, Locator locator, int timeoutMs) {
        page.waitForFunction(
                "el => !el.disabled",
                locator,
                new Page.WaitForFunctionOptions().setTimeout(timeoutMs)
        );
    }

    public static void waitForCountGreaterThanZero(Locator locator, int timeoutMs) {
        locator.page().waitForFunction(
                "loc => loc.count() > 0",
                locator,
                new Page.WaitForFunctionOptions().setTimeout(timeoutMs)
        );
    }

    public static void safeClick(Locator locator, int timeoutMs) {
        waitForVisible(locator, timeoutMs);
        locator.scrollIntoViewIfNeeded();
        locator.click();
    }

    public static void safeHover(Locator locator, int timeoutMs) {
        waitForVisible(locator, timeoutMs);
        locator.scrollIntoViewIfNeeded();
        locator.hover(new Locator.HoverOptions().setForce(true));
    }
}
