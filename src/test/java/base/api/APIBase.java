package base.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.trident.playwright.utils.ParseTheTimeFormat;
import com.trident.playwright.utils.ReadPropertiesFile;

import java.util.*;

public class APIBase {
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

    public static Set<String> fetchApiData(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode dataArray = root.path("equipKpis")
                .path(0)
                .path("kpis")
                .path(0)
                .path("data");
        Set<String> apiValues = new LinkedHashSet<>();
        Iterator<JsonNode> iterator = dataArray.elements();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            String gmtTimestamp = node.get("timestamp").asText();

            JsonNode valueNode = node.get("doubleValue");
            if (!valueNode.isNull()) {
                double value = valueNode.asDouble();
                String valueFromAPI = ParseTheTimeFormat.changeTimeFormatAndValue(gmtTimestamp,value);
                apiValues.add(valueFromAPI);
            }
        }
        System.out.println(apiValues.size());
        return apiValues;
    }

    public static void closeApi() {
        request.dispose();
        playwright.close();
    }
}
