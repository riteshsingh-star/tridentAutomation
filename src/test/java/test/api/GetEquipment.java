package test.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import utils.ReadPropertiesFile;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetEquipment extends APIBase {


    @Test
    public void getEquipmentIDAndData() {
        String equipmentID= ReadPropertiesFile.get("equipmentID");
        APIResponse response = request.get("/web/api/kpi-implementation/equipment/"+equipmentID);
        Assert.assertEquals(response.status(), 200);
        System.out.println(response.text());
    }


}
