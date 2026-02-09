# Trident Framework API Documentation

## Overview

The Trident Framework provides comprehensive API testing capabilities using Microsoft Playwright's API testing features. This documentation covers the API testing architecture, available endpoints, request/response handling, and best practices.

## API Testing Architecture

### Base API Infrastructure

The framework's API testing is built around the `APIBase` class, which provides:

- **Authentication Management**: Automatic token-based authentication
- **Request Context**: Centralized API request handling
- **Response Parsing**: Built-in JSON response parsing utilities
- **Error Handling**: Comprehensive error management

### Core Components

```java
public class APIBase {
    protected static Playwright playwright;
    protected static APIRequestContext request;
    
    // Authentication setup
    public void initApi() { ... }
    
    // Request methods
    public static APIResponse postApiRequest(Map<String, Object> bodyMap, String urlPath) { ... }
    public static APIResponse readJsonFileForApiRequestPayload(String fileName, String urlPath) { ... }
    
    // Response parsing
    public static Map<String, String> fetchApiData(String responseJson, String rootPath, 
                                                  int parameterIndexNumber, String parameterName, 
                                                  int dataIndexNo, String parameterDataName, 
                                                  boolean isComparingWebGraph) { ... }
}
```

## Available API Endpoints

### Configuration

API endpoints are configured in `config.properties`:

```properties
# Base URLs
sit.APIBaseURL=https://sit-ipf.infinite-uptime.com
uat.APIBaseURL=https://uat-new-ipf.infinite-uptime.com

# API Paths
timeSeriesPathURL=/query/api/kpis/timeseries
rawParameterPathURL=/query/api/raw-parameters/timeseries
aggregatePathURL=/query/api/kpis/aggregates
```

### Supported Endpoints

#### 1. KPI Time Series Data

**Endpoint**: `/query/api/kpis/timeseries`

**Method**: POST

**Description**: Retrieves time series data for Key Performance Indicators

**Request Structure**:
```json
{
  "equipKpis": [
    {
      "id": 4248,
      "kpiParamDefIds": [9]
    }
  ],
  "dateRange": {
    "startTs": "2026-01-28T04:30:00.000Z",
    "endTs": "2026-01-29T04:30:00.000Z"
  },
  "granularityInMillis": 60000,
  "addMissingTimestamps": true
}
```

**Usage Example**:
```java
public static String getKpiData() {
    Map<String, Object> payload = new HashMap<>();
    Map<String, Object> equipKpis = new HashMap<>();
    equipKpis.put("id", 4248);
    equipKpis.put("kpiParamDefIds", List.of(9));
    payload.put("equipKpis", List.of(equipKpis));
    
    Map<String, Object> dateRange = new HashMap<>();
    dateRange.put("startTs", "2026-01-28T04:30:00.000Z");
    dateRange.put("endTs", "2026-01-29T04:30:00.000Z");
    payload.put("dateRange", dateRange);
    
    payload.put("granularityInMillis", 60000L);
    payload.put("addMissingTimestamps", true);
    
    APIResponse response = postApiRequest(payload, timeSeriesPathURL);
    Assert.assertEquals(response.status(), 200);
    return response.text();
}
```

#### 2. Raw Parameter Time Series Data

**Endpoint**: `/query/api/raw-parameters/timeseries`

**Method**: POST

**Description**: Retrieves time series data for raw parameters

**Request Structure**:
```json
{
  "equipRawParams": [
    {
      "id": 4248,
      "rawParamDefIds": [1, 2, 3]
    }
  ],
  "dateRange": {
    "startTs": "2026-01-28T04:30:00.000Z",
    "endTs": "2026-01-29T04:30:00.000Z"
  },
  "granularityInMillis": 60000,
  "addMissingTimestamps": true
}
```

#### 3. KPI Aggregates

**Endpoint**: `/query/api/kpis/aggregates`

**Method**: POST

**Description**: Retrieves aggregated KPI data (min, max, mean, etc.)

**Request Structure**:
```json
{
  "equipKpis": [
    {
      "id": 4248,
      "kpiParamDefIds": [9]
    }
  ],
  "dateRange": {
    "startTs": "2026-01-28T04:30:00.000Z",
    "endTs": "2026-01-29T04:30:00.000Z"
  },
  "granularityInMillis": 60000
}
```

#### 4. Equipment Data

**Endpoint**: `/web/api/kpi-implementation/equipment/{equipmentId}`

**Method**: GET

**Description**: Retrieves equipment details by ID

**Usage Example**:
```java
@Test
public void getEquipmentIDAndData() {
    String equipmentID = ReadPropertiesFile.get("equipmentID");
    APIResponse response = request.get("/web/api/kpi-implementation/equipment/" + equipmentID);
    Assert.assertEquals(response.status(), 200);
    System.out.println(response.text());
}
```

## Data Models (POJOs)

### Equipment KPI

```java
public record EquipKpi(
    int id,
    List<Integer> kpiParamDefIds
) {}
```

### Date Range

```java
public record DateRange(
    String startTs,
    String endTs
) {}
```

### Equipment KPI Request

```java
public record EquipKpiRequest(
    List<EquipKpi> equipKpis,
    DateRange dateRange,
    int granularityInMillis,
    boolean addMissingTimestamps
) {}
```

### Raw Request

```java
public record RawRequest(
    List<EquipRawParams> equipRawParams,
    DateRange dateRange,
    int granularityInMillis,
    boolean addMissingTimestamps
) {}
```

## Request Building Strategies

### 1. Map-Based Approach

```java
Map<String, Object> payload = new HashMap<>();
payload.put("equipKpis", List.of(equipKpis));
payload.put("dateRange", dateRange);
payload.put("granularityInMillis", 60000L);
payload.put("addMissingTimestamps", true);
```

### 2. POJO-Based Approach

```java
EquipKpi equipKpi = new EquipKpi(4248, List.of(9));
DateRange dateRange = new DateRange("2026-01-26T04:30:00.000Z", "2026-01-27T04:30:00.000Z");
EquipKpiRequest request = new EquipKpiRequest(List.of(equipKpi), dateRange, 60000, true);

ObjectMapper mapper = new ObjectMapper();
Map<String, Object> payload = mapper.convertValue(request, Map.class);
```

### 3. JSON File-Based Approach

```java
APIResponse response = readJsonFileForApiRequestPayload("sampleRequest", "/api/endpoint");
```

## Response Handling

### Response Parsing Utilities

#### fetchApiData Method

Extracts specific data from API responses:

```java
public static Map<String, String> fetchApiData(String responseJson, 
                                               String rootPath, 
                                               int parameterIndexNumber, 
                                               String parameterName, 
                                               int dataIndexNo, 
                                               String parameterDataName, 
                                               boolean isComparingWebGraph) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(responseJson);
    JsonNode dataArray = root.path(rootPath)
                             .path(parameterIndexNumber)
                             .path(parameterName)
                             .path(dataIndexNo)
                             .path(parameterDataName);
    
    Map<String, String> apiValues = new LinkedHashMap<>();
    Iterator<JsonNode> iterator = dataArray.elements();
    
    while (iterator.hasNext()) {
        JsonNode node = iterator.next();
        String gmtTimestamp = node.get("timestamp").asText();
        JsonNode valueNode = node.get("doubleValue");
        
        if (!valueNode.isNull()) {
            String value = valueNode.asText();
            String convertedTimeStamp = ParseTheTimeFormat.changeTimeFormat(gmtTimestamp);
            
            if(isComparingWebGraph) {
                changedDoubleValue = ParseTheTimeFormat.formatStringTo2Decimal(value);
                apiValues.put(convertedTimeStamp, changedDoubleValue);
            } else {
                apiValues.put(convertedTimeStamp, value);
            }
        }
    }
    return apiValues;
}
```

#### Statistics Extraction

```java
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
```

## Authentication

### Token-Based Authentication

The framework uses Bearer token authentication:

```java
public void initApi() {
    String baseURIIU = ReadPropertiesFile.get("APIBaseURL");
    String authToken = ReadPropertiesFile.get("authToken");
    String orgID = ReadPropertiesFile.get("X-ORG-ID");
    playwright = Playwright.create();

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("X-ORG-ID", orgID);
    headers.put("Authorization", "Bearer " + authToken);

    request = playwright.request().newContext(
        new APIRequest.NewContextOptions()
            .setBaseURL(baseURIIU)
            .setExtraHTTPHeaders(headers)
    );
}
```

### Configuration

Authentication credentials are stored in `config.properties`:

```properties
authToken=eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJEZjBqaWVZY2dYajRldnBsek5JYldjQjFHb3g1d3dBWUZWS0xPWUM5c084In0...
X-ORG-ID=901
```

## Test Data Management

### JSON Test Data Files

Test data is stored in `src/test/resources/testData/`:

- `adminFlow.json`: Admin workflow test data
- `baseDataCreation.json`: Base data creation scenarios
- `dashboard.json`: Dashboard-related test data
- `equipmentVerification.json`: Equipment verification data
- `rawParameter.json`: Raw parameter test data
- `userAndDeviceData.json`: User and device creation data

### API Request Payloads

API request payloads are stored in `src/test/resources/APIRequests/`:

```java
public static APIResponse readJsonFileForApiRequestPayload(String fileName, String urlPath) throws IOException {
    String body = Files.readString(
        Paths.get("src/test/resources/APIRequests/" + fileName + ".json"), 
        StandardCharsets.UTF_8
    );
    return request.post(urlPath, RequestOptions.create().setData(body.getBytes(StandardCharsets.UTF_8)));
}
```

## Error Handling

### Response Validation

```java
APIResponse response = postApiRequest(payload, pathURL);
Assert.assertEquals(response.status(), 200);

// Check response body
if (response.status() != 200) {
    log.error("API request failed with status: " + response.status());
    log.error("Response body: " + response.text());
}
```

### Exception Handling

```java
try {
    APIResponse response = postApiRequest(payload, pathURL);
    // Process response
} catch (Exception e) {
    log.error("API request failed", e);
    throw new RuntimeException("Failed to execute API request", e);
}
```

## Best Practices

### 1. Request Building

- Use POJOs for complex requests to ensure type safety
- Validate request payloads before sending
- Use descriptive variable names for clarity

### 2. Response Handling

- Always validate response status codes
- Parse responses using framework utilities
- Handle null values gracefully

### 3. Test Organization

- Group related API tests in separate classes
- Use descriptive test method names
- Implement proper setup and teardown

### 4. Data Management

- Externalize test data from test code
- Use environment-specific configurations
- Implement data cleanup strategies

### 5. Authentication

- Store tokens securely
- Implement token refresh mechanisms
- Use environment variables for sensitive data

## Example Test Cases

### KPI Data Retrieval Test

```java
public class GetKpiData extends APIBase {
    
    @Test
    public void testKpiDataRetrieval() throws IOException {
        String responseJson = getKpiData();
        Map<String, String> apiValues = getKpiDataUsingMap();
        
        // Assertions
        Assert.assertNotNull(apiValues);
        Assert.assertFalse(apiValues.isEmpty());
        
        // Log results
        apiValues.forEach((timestamp, value) -> 
            log.info("Timestamp: {}, Value: {}", timestamp, value));
    }
    
    @Test
    public void testKpiDataWithPOJO() throws IOException {
        String responseJson = GetKpiDataPojo();
        Map<String, String> apiValues = getKpiDataUsingMapPojo();
        
        Assert.assertNotNull(apiValues);
        Assert.assertTrue(apiValues.size() > 0);
    }
}
```

### Equipment Data Test

```java
public class GetEquipment extends APIBase {
    
    @Test
    public void testEquipmentRetrieval() {
        String equipmentID = ReadPropertiesFile.get("equipmentID");
        APIResponse response = request.get("/web/api/kpi-implementation/equipment/" + equipmentID);
        
        Assert.assertEquals(response.status(), 200);
        
        // Parse and validate response
        String responseBody = response.text();
        Assert.assertNotNull(responseBody);
        Assert.assertFalse(responseBody.isEmpty());
    }
}
```

## Integration with Web Tests

The framework supports API and web test integration:

```java
public class GraphVsApiValidationTest extends BaseTest {
    
    @Test
    public void validateGraphWithApiData() throws IOException {
        // Get data from UI
        Dashboard dashboard = new Dashboard(page, context);
        Map<String, String> graphData = dashboard.getGraphData();
        
        // Get data from API
        Map<String, String> apiData = GetKpiData.getKpiDataUsingMap();
        
        // Compare data
        Assert.assertEquals(graphData.size(), apiData.size(), 
            "Graph and API data counts should match");
    }
}
```

---

This API documentation provides a comprehensive guide for working with the Trident Framework's API testing capabilities. For more specific implementation details, refer to the source code and test examples.
