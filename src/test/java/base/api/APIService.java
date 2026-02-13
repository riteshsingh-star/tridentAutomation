package base.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static base.api.APIBase.log;
import static factory.ApiFactory.getRequest;

public class APIService {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static APIResponse postApiRequest(Map<String, Object> bodyMap, String urlPath) {
        try {
            log.info("Sending POST request using Map to {}", urlPath);
            String body = objectMapper.writeValueAsString(bodyMap);
            return getRequest().post(urlPath, RequestOptions.create().setData(body.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("Failed to create API request body from Map", e);
            throw new RuntimeException("Failed to create API request body from Map", e);
        }
    }

    public static APIResponse postApiRequest(Object bodyObj, String urlPath) {
        try {
            log.info("Sending POST request to {}", urlPath);
            String body = objectMapper.writeValueAsString(bodyObj);
            log.info("Request Sent for {}", urlPath);
            return getRequest().post(urlPath, RequestOptions.create().setData(body.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("Failed to create API request body from Object", e);
            throw new RuntimeException("Failed to serialize API request body", e);
        }
    }
}
