package test.api;

import base.api.APIBase;
import com.microsoft.playwright.APIResponse;
import config.EnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import static factory.ApiFactory.getRequest;

public class GetEquipment extends APIBase {


    @Test
    public void getEquipmentIDAndData() {
        String equipmentID= EnvironmentConfig.getEquipmentID();
        APIResponse response = getRequest().get("/web/api/kpi-implementation/equipment/"+equipmentID);
        Assert.assertEquals(response.status(), 200);
        System.out.println(response.text());
    }


}
