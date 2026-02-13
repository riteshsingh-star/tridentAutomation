package page.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;

public class GetKpiRequest {


    public static JsonNode getKpiNode(APIRequestContext request, int definitionId, int equipmentId) {

        String endpoint="/web/api/kpi-implementation?definition-id=" + definitionId + "&equipment-id=" + equipmentId;
        APIResponse response = request.get(endpoint);
        Assert.assertEquals(response.status(), 200, "KPI API failed");
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(response.text());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse KPI API response", e);
        }
    }
}
