package page.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import utils.ReadPropertiesFile;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GetEquipment extends APIBase {

    @BeforeClass
    public void setup() {
        initApi();
    }

    @Test
    public void getEquipmentIDAndData() {
        String equipmentID= ReadPropertiesFile.get("equipmentID");
        APIResponse response = request.get("/web/api/kpi-implementation/equipment/"+equipmentID);
        Assert.assertEquals(response.status(), 200);
        System.out.println(response.text());
    }

    @AfterClass
    public void tearDown() {
        closeApi();
    }
}
