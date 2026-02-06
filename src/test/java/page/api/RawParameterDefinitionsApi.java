package page.api;

import base.api.APIBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import pojo.RawParamInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RawParameterDefinitionsApi extends APIBase {

    public static List<RawParamInfo> getRawParamsByPlantId(int plantId) throws IOException {

        APIResponse response = request.get("/web/api/rawParameterDefinitions?plantId=" + plantId);
        Assert.assertEquals(response.status(), 200);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.text());

        List<RawParamInfo> rawParams = new ArrayList<>();

        for (JsonNode node : rootNode) {
            int rawParamEquipmentId = node.path("rawParameterEquipmentId").asInt();
            int rawParamId = node.path("id").asInt();

            rawParams.add(new RawParamInfo(rawParamEquipmentId, rawParamId));
        }

        return rawParams;
    }
}
