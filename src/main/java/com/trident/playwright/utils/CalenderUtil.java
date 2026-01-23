package com.trident.playwright.utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CalenderUtil {

    public static void selectDate(Page page,
                                  int year, String month, String day) {


        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Calender")).click();

        // 2. Loop until desired month & year visible
        while (true) {
            String visibleMonth =
                    page.locator("select-none font-medium text-sm rdp-caption_label").innerText();
            // example: "January 2026"

            if (visibleMonth.contains(month) && visibleMonth.contains(String.valueOf(year))) {
                break;
            }
            page.locator("//button[@aria-label='Go to the Previous Month']").click();
        }

        // 3. Select day
        page.getByText(day).click();
//        page.locator(
//                "//div[contains(@class,'react-datepicker__day') and text()='" + day + "']"
//        ).click();
    }
}
