package test.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import config.EnvironmentConfig;
import org.testng.annotations.Test;

import static factory.ApiFactory.getRequest;
import static utils.AssertionUtil.assertEquals;

public class GetEquipment extends APIBase {


    @Test
    public void getEquipmentIDAndData() {
        String equipmentID= EnvironmentConfig.getEquipmentID();
        APIResponse response = getRequest().get("/web/api/kpi-implementation/equipment/"+equipmentID);
        assertEquals(response.status(), 200, "Equipment API should return status 200");
        System.out.println(response.text());
    }


}
