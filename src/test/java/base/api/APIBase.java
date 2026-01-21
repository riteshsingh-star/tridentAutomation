package base.api;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.trident.playwright.utils.ReadPropertiesFile;

import java.util.HashMap;
import java.util.Map;

public class APIBase {
    protected static Playwright playwright;
    protected static APIRequestContext request;

    public static void initApi() {
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


    public static void closeApi() {
        request.dispose();
        playwright.close();
    }
}
