package base.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.trident.playwright.utils.ParseTheTimeFormat;
import com.trident.playwright.utils.ReadPropertiesFile;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class APIBase {

    @BeforeClass
    public void setup() {
        initApi();
    }
    protected static Playwright playwright;
    protected static APIRequestContext request;

    public void initApi() {
        String baseURIIU = ReadPropertiesFile.get("APIBaseURL");
        String authToken = ReadPropertiesFile.get("authToken");
        playwright = Playwright.create();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-ORG-ID", "901");
        headers.put("Authorization", "Bearer "+authToken);

        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(baseURIIU)
                        .setExtraHTTPHeaders(headers)
        );
    }

    public static Map<String, String> fetchApiData(String responseJson, String rootPath,int parameterIndexNumber, String parameterName, int dataIndexNo, String parameterDataName) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseJson);
        JsonNode dataArray = root.path(rootPath)
                .path(parameterIndexNumber)
                .path(parameterName)
                .path(dataIndexNo)
                .path(parameterDataName);
        Map<String,String> apiValues = new LinkedHashMap<>();
        Iterator<JsonNode> iterator = dataArray.elements();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            String gmtTimestamp = node.get("timestamp").asText();
            JsonNode valueNode = node.get("doubleValue"); //Double, we cannot as it is breaking the null comparison
            if (!valueNode.isNull()) {
                String value = valueNode.asText();
                String convertedTimeStamp=ParseTheTimeFormat.changeTimeFormat(gmtTimestamp);
                String changedDoubleValue=ParseTheTimeFormat.formatStringTo2Decimal(value);
                apiValues.put(convertedTimeStamp,changedDoubleValue);
            }
        }
        return apiValues;
    }

    public static APIResponse readJsonFileForApiRequestPayload(String fileName, String urlPath) throws IOException {
        String body = Files.readString(
                Paths.get("src/test/resources/APIRequests/"+fileName+".json"), StandardCharsets.UTF_8);
        return request.post(urlPath, RequestOptions.create().setData(body.getBytes(StandardCharsets.UTF_8)));
    }

    public static Map<String, Map<String, Double>> getAllStatistics(JsonNode rootNode) {
        Map<String, Map<String, Double>> result = new LinkedHashMap<>();
        for (JsonNode equip : rootNode.path("equipments")) {
            int eqId = equip.path("id").asInt();
            for (JsonNode param : equip.path("rawParameters")) {
                int paramId = param.path("rawParamDefId").asInt();
                JsonNode stats = param.path("statistics");
                Map<String, Double> s = new HashMap<>();
                s.put("max", stats.path("maxValue").asDouble());
                s.put("min", stats.path("minValue").asDouble());
                s.put("mean", stats.path("meanValue").asDouble());
                s.put("median", stats.path("medianValue").asDouble());
                s.put("stdDev", stats.path("standardDeviationValue").asDouble());
                result.put(eqId + "_" + paramId, s);
            }
        }
        return result;
    }

    public static Double getRawParameterValue(JsonNode rootNode, int equipmentId, int rawParamDefId) {
        JsonNode equipments = rootNode.path("equipments");
        for (JsonNode equip : equipments) {
            if (equip.path("equipmentId").asInt() == equipmentId) {
                for (JsonNode param : equip.path("rawParameters")) {
                    if (param.path("rawParameterDefId").asInt() == rawParamDefId) {
                        return param.path("data").path("value").asDouble();
                    }
                }
            }
        }
        return null;
    }

    @AfterClass
    public void tearDown() {
        closeApi();
    }

    public static void closeApi() {
        request.dispose();
        playwright.close();
    }
}
