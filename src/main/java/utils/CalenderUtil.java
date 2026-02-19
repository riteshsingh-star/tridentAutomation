package utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Utility class for handling calendar date selection
 * in Playwright UI automation.
 * Provides reusable logic to:
 * - Open calendar widget
 * - Navigate to desired month/year
 * - Select specific day
 */

public class CalenderUtil {

    /**
     * Selects a specific date from a calendar component.
     * @param page  Playwright Page instance
     * @param year  Target year
     * @param month Target month
     * @param day   Target day
     */

    public static void selectDate(Page page, int year, String month, String day) {
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Calender")).click();
        while (true) {
            String visibleMonth =
                    page.locator("select-none font-medium text-sm rdp-caption_label").innerText();
            if (visibleMonth.contains(month) && visibleMonth.contains(String.valueOf(year))) {
                break;
            }
            page.locator("//button[@aria-label='Go to the Previous Month']").click();
        }
        page.getByText(day).click();
    }
}
