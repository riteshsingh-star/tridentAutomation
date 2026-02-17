package factory;

import com.microsoft.playwright.*;
import config.EnvironmentConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ApiFactory {


    protected static final Logger log = LogManager.getLogger(ApiFactory.class);
    private static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static ThreadLocal<APIRequestContext> request = new ThreadLocal<>();
    private static ThreadLocal<Map<String, String>> headers = new ThreadLocal<>();
    private static ThreadLocal<String> baseURI = new ThreadLocal<>();

    public static void initApi() {

        String baseURIIU = EnvironmentConfig.getBaseUrl();
        String authToken = EnvironmentConfig.getAuthToken();
        String orgID = EnvironmentConfig.getOrgId();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("X-ORG-ID", orgID);
        headerMap.put("Authorization", "Bearer " + authToken);

        headers.set(headerMap);
        baseURI.set(baseURIIU);
        log.info("Request headers configured");
    }

    public static void createRequestContext() {
        Playwright pw = Playwright.create();
        playwright.set(pw);
        APIRequestContext requestContext = pw.request().newContext(new APIRequest.NewContextOptions().setBaseURL(baseURI.get()).setExtraHTTPHeaders(headers.get()));
        request.set(requestContext);
        log.info("API request context created");
    }

    public static APIRequestContext getRequest() {
        return request.get();
    }

    public static Playwright getPlaywright() {
        return playwright.get();
    }


    public static void closeApi() {

        try {
            if (request.get() != null) {
                request.get().dispose();
                request.remove();
            }
            if (playwright.get() != null) {
                playwright.get().close();
                playwright.remove();
            }
            headers.remove();
            baseURI.remove();
            log.info("API Session Closed");

        } catch (Exception e) {
            log.error("Error during API cleanup", e);
            throw new RuntimeException("Error during API cleanup", e);
        }
    }
}
