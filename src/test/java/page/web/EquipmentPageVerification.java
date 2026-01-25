package page.web;

import base.web.BasePage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

public class EquipmentPageVerification extends BasePage {

    public EquipmentPageVerification(Page page) {
        super(page);
    }

    public void openEquipmentAndAddKPI(String equipmentName, String measureName) throws InterruptedException {
        getByRoleLink("Equipment");
        getByPlaceholder("Search...", equipmentName);
        getByRoleLink(equipmentName);
        page.getByRole(AriaRole.BUTTON)
                .getByText(Pattern.compile("\\d{2} .* AM|PM", Pattern.CASE_INSENSITIVE)).click();
        getByRoleButton("Quick Links");
        getByText("Last month");
        getByRoleButton("Apply");
        getByRoleButton("Add parameters");
        getByPlaceholder("Search measures...", measureName);
       /* page.getByRole(AriaRole.CHECKBOX,
                new Page.GetByRoleOptions().setName(Pattern.compile("Availability", Pattern.CASE_INSENSITIVE))
        ).click();*/
        waitAndClick("//span[text()='"+measureName+"']");

        page.keyboard().press("Escape");
        page.getByTitle("Show in trend panel").click();
        getChartData(0,2);
    }
}
