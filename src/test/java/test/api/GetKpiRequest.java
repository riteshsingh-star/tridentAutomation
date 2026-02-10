package test.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;

public class GetKpiRequest {


<<<<<<< HEAD
    public static JsonNode getKpiNode(APIRequestContext request, int definitionId, int equipmentId) {
=======
    public static JsonNode getKpiNode(APIRequestContext request, String definitionId, String equipmentId) {
<<<<<<< HEAD
        APIResponse response = request.get("/web/api/kpi-implementation?definition-id=" + definitionId + "&equipment-id=" + equipmentId);
=======
>>>>>>> 3f3834cc5487a413030fada6c41d064ddcd00f26

        APIResponse response = request.get(
                "/web/api/kpi-implementation?definition-id=" + definitionId + "&equipment-id=" + equipmentId
        );
>>>>>>> 5f16c47df3cd4cabf2114db2325d22c995811303

        Assert.assertEquals(response.status(), 200, "KPI API failed");
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(response.text());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse KPI API response", e);
        }
    }
<<<<<<< HEAD

    public static String getLclUclType(APIRequestContext request, String definitionId, String equipmentId) {
        return getKpiNode(request, definitionId, equipmentId).path("lclUclType").asText(null);
    }

    public static String getFixedLcl(APIRequestContext request, String definitionId, String equipmentId) {
        return getKpiNode(request, definitionId, equipmentId).path("lcl").asText(null);
    }

    public static String getFixedUcl(APIRequestContext request, String definitionId, String equipmentId) {
        return getKpiNode(request, definitionId, equipmentId).path("ucl").asText(null);
    }
=======
>>>>>>> 5f16c47df3cd4cabf2114db2325d22c995811303
}
