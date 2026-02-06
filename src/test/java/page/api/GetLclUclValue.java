package page.api;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.*;
import base.api.APIBase;
import utils.ReadPropertiesFile;
import utils.Stats;
import java.io.IOException;
import java.util.Map;

public class GetLclUclValue extends APIBase{

    Map<String, String> rawParameterData;

    @Test
    public void getSTDMeanLCLUCL() throws IOException {

        APIResponse response = request.get("/web/api/rawParameterDefinitions?plantId=839");
        Assert.assertEquals(response.status(), 200);

        String responseBody = response.text();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseBody);

        for (JsonNode node : rootNode) {

            int equipmentId = node.path("rawParameterEquipmentId").asInt();
            int id = node.path("id").asInt();

            int expectedEquipmentId = Integer.parseInt(ReadPropertiesFile.get("equipmentId"));
            int expectedRawParamDefId = Integer.parseInt(ReadPropertiesFile.get("rawParamDefId"));

            if (equipmentId == expectedEquipmentId && id == expectedRawParamDefId) {

                String lclUclType = node.path("lclUclType").asText();
                String lcl = null;
                String ucl = null;

                System.out.println("lclUclType: " + lclUclType);
                rawParameterData = GetRawParameterData.getRawParameterDataValues();

                if (rawParameterData == null || rawParameterData.isEmpty()) {
                    Assert.fail("No raw parameter data available to calculate mean/std deviation");
                }
                double mean = Stats.calculateMean(rawParameterData);
                double stdDev = Stats.calculateStdDev(rawParameterData);

                System.out.println("Mean: " + mean);
                System.out.println("Std Dev: " + stdDev);

                switch (lclUclType) {
                    case "STATS":
                        System.out.println("Calculating LCL and UCL based on statistics...");

                        ucl = String.valueOf(Stats.calculateUCL(mean, stdDev));
                        lcl = String.valueOf(Stats.calculateLCL(mean, stdDev));

                        System.out.println("Calculated LCL: " + lcl);
                        System.out.println("Calculated UCL: " + ucl);
                        break;

                    case "FIXED":

                        lcl = node.path("lcl").asText();
                        ucl = node.path("ucl").asText();
                        System.out.println("LCL : " + lcl);
                        System.out.println("UCL : " + ucl);
                        break;

                    case "NONE":
                        lcl = null;
                        ucl = null;
                        break;

                    default:
                        System.out.println("Unknown lclUclType: " + lclUclType);
                }

                Assert.assertNotNull(lcl, "LCL should not be null");
                Assert.assertNotNull(ucl, "UCL should not be null");


            }
        }
    }

    @AfterClass
    public void teardown() {
        if (request != null) {
            request.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
