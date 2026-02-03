import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

public class TestApi {

    Playwright playwright;
    APIRequestContext request;

    @BeforeClass
    void setup() {
        playwright = Playwright.create();

        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://sit-ipf.infinite-uptime.com")
                        .setExtraHTTPHeaders(
                                Map.of(
                                        "X-Org-ID", "901",
                                        "Content-Type", "application/json",
                                        "Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUaWdTUzlleFdjemdtR2dyU1dRc3VBSVNnY0d2LWZUb2xzYjFpQ1A0bXVzIn0.eyJleHAiOjE3NzAxNTExODMsImlhdCI6MTc3MDEwNzk4NCwiYXV0aF90aW1lIjoxNzcwMTA3OTgzLCJqdGkiOiJjMTZjNGZkZi03MTE1LTQ5NGItYjQ0NS1iYWQyNWVmNWM2M2IiLCJpc3MiOiJodHRwczovL3NpdC1pZGVudGl0eS5pbmZpbml0ZS11cHRpbWUuY29tL3JlYWxtcy9pZGFwIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJicm9rZXIiLCJhY2NvdW50Il0sInN1YiI6ImY6M2ZiMzliMDctYzBjYS00MDJhLWJhOGItOTBlNWJiODkzZjEzOjEyNTYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhZG1pbi1jb3ZhY3NpcyIsInNlc3Npb25fc3RhdGUiOiI1YjE1N2UzNC0xYWUwLTRlOGMtODc3OS1kMmYzOWNmODAzZDYiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiaW1wZXJzb25hdGlvbiIsIm9mZmxpbmVfYWNjZXNzIiwicmVhZF90b2tlbiIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJpbXBlcnNvbmF0aW9uIl19LCJicm9rZXIiOnsicm9sZXMiOlsicmVhZC10b2tlbiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6IjViMTU3ZTM0LTFhZTAtNGU4Yy04Nzc5LWQyZjM5Y2Y4MDNkNiIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IkNvdmFjc2lzX0RldiBBZG1pbiIsImlkYXBfcm9sZSI6IlJPTEVfQURNSU4iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJjb3ZhY3Npc19hZG1pbkB0ZWNocHJlc2NpZW50LmNvbSIsImdpdmVuX25hbWUiOiJDb3ZhY3Npc19EZXYiLCJsb2NhbGUiOiJlbiIsImZhbWlseV9uYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImNvdmFjc2lzX2FkbWluQHRlY2hwcmVzY2llbnQuY29tIn0.lOzy-Bgn5JOWfuoIfLYeYgA_mQy2UU3QDYvyf-frMNbLju6emOUUa-hqw29WC5XwDdko8HgQA_IhA3aOafC5fzB2cLr77Ia0Zu69gFt0srsjkdmmlATFDd7Qqj9o5FyTdSfq87P-TJmspExQDNZISPTN6shNaTOsgGKUFbengSUfKeGLYn-AC3tNP3K8V1S1GIPASXMEiEWWgUwGdwEJogrwWLpDmzsTm6IviI-1TC7wLrjl0_7BuOAeyv3Z_oFUJk8x9m-zJZsNInS3KYNzycpTXo5WtnyWyUMcQ0Wtj-IFZWyQJ7OvkjbCSaQBy8P0f8IAS4XyPNkkrZxO56O2dA"
                                )
                        )
        );
    }

    @Test
    void getKpiTimeseries() {
        String body = """
        {
          "equipKpis": [
            {
              "id": 662,
              "kpiParamDefIds": [21]
            }
          ],
          "dateRange": {
            "startTs": "2026-01-26T04:30:00.000Z",
            "endTs": "2026-01-27T04:30:00.000Z"
          },
          "granularityInMillis": 60000,
          "addMissingTimestamps": true
        }
        """;

        APIResponse response = request.post(
                "/query/api/kpis/timeseries",
                RequestOptions.create().setData(body)
        );

        System.out.println("Status: " + response.status());
        System.out.println("Response:\n" + response.text());

        Assert.assertEquals(response.status(), 200, "API failed");
    }

    @AfterClass
    void teardown() {
        request.dispose();
        playwright.close();
    }
}
