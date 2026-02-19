package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Utility class providing reusable wait helpers
 * - Element state synchronization
 * - Safe interaction (click/hover)
 * - Dynamic element loading
 */

public class WaitUtils {

    /**
     * Waits until the locator is attached to the DOM.
     * @param locator   Playwright locator
     * @param timeoutMs Maximum wait time in milliseconds
     */

    public static void waitForAttached(Locator locator, int timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(timeoutMs));
    }

    /**
     * Waits until the locator becomes visible.
     * @param locator   Playwright locator
     * @param timeoutMs Maximum wait time in milliseconds
     */

    public static void waitForVisible(Locator locator, int timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeoutMs));
    }

    /**
     * Waits until the locator becomes hidden.
     * @param locator   Playwright locator
     * @param timeoutMs Maximum wait time in milliseconds
     */

    public static void waitForHidden(Locator locator, int timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(timeoutMs));
    }

    /**
     * Waits until the element is enabled and visible
     * by checking computed style visibility.
     * @param page      Playwright page instance
     * @param locator   Target locator
     * @param timeoutMs Maximum wait time in milliseconds
     */

    public static void waitForEnabled(Page page, Locator locator, int timeoutMs) {
        page.waitForFunction(
                "el => el && window.getComputedStyle(el).visibility !== 'hidden'",
                locator.elementHandle(),
                new Page.WaitForFunctionOptions().setTimeout(timeoutMs)
        );
    }

    /**
     * Waits until the locator count becomes greater than zero.
     * Useful for dynamic lists/grids.
     * @param locator   Playwright locator
     * @param timeoutMs Maximum wait time in milliseconds
     */

    public static void waitForCountGreaterThanZero(Locator locator, int timeoutMs) {
        locator.page().waitForFunction("loc => loc.count() > 0", locator, new Page.WaitForFunctionOptions().setTimeout(timeoutMs));
    }

    /**
     * Safely clicks on an element by:
     * - Waiting for visibility
     * - Scrolling into view if needed
     * - Performing click
     * @param locator   Playwright locator
     * @param timeoutMs Maximum wait time in milliseconds
     */

    public static void safeClick(Locator locator, int timeoutMs) {
        waitForVisible(locator, timeoutMs);
        locator.scrollIntoViewIfNeeded();
        locator.click();
    }

    /**
     * Safely hovers over an element:
     * - Waiting for visibility
     * - Scrolling into view
     * - Forcing hover action
     * @param locator   Playwright locator
     * @param timeoutMs Maximum wait time in milliseconds
     */

    public static void safeHover(Locator locator, int timeoutMs) {
        waitForVisible(locator, timeoutMs);
        locator.scrollIntoViewIfNeeded();
        locator.hover(new Locator.HoverOptions().setForce(true));
    }
}
