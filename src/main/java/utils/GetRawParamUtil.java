package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;

public class GetRawParamUtil {

    public static JsonNode getRawParamNode(APIRequestContext request, int plantId, int equipmentId, int rawParamDefId) {

        APIResponse response = request.get("/web/api/rawParameterDefinitions?plantId=" + plantId);
        Assert.assertEquals(response.status(), 200, "Raw Parameter API failed");

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.text());

            for (JsonNode node : rootNode) {
                int apiEquipmentId = node.path("rawParameterEquipmentId").asInt();
                int apiRawParamDefId = node.path("id").asInt();

                if (apiEquipmentId == equipmentId && apiRawParamDefId == rawParamDefId) {
                    return node;
                }
            }

            throw new RuntimeException("No matching raw parameter found for equipmentId="
                    + equipmentId + " and rawParamDefId=" + rawParamDefId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Raw Parameter API response", e);
        }
    }

    public static String getLclUclType(APIRequestContext request, int plantId, int equipmentId, int rawParamDefId) {
        return getRawParamNode(request, plantId, equipmentId, rawParamDefId)
                .path("lclUclType").asText(null);
    }

    public static String getFixedLcl(APIRequestContext request, int plantId, int equipmentId, int rawParamDefId) {
        return getRawParamNode(request, plantId, equipmentId, rawParamDefId)
                .path("lcl").asText(null);
    }

    public static String getFixedUcl(APIRequestContext request, int plantId, int equipmentId, int rawParamDefId) {
        return getRawParamNode(request, plantId, equipmentId, rawParamDefId)
                .path("ucl").asText(null);
    }
}

