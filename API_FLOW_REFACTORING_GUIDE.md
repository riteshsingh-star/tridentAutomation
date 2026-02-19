# API Flow & Data Management Refactoring Guide

## Overview
This guide shows how to refactor the API flow to decouple test data from test logic, making the framework enterprise-scalable.

---

## Current API Flow Analysis

### Current Architecture (Problems)
```
┌──────────────────────────────────────────────────────────────┐
│                        Test Class                            │
│  ────────────────────────────────────────────────────────     │
│  - Hardcoded test parameters (equipmentId, kpiID, etc.)     │
│  - LocalDateTime objects defined in test class              │
│  - API payload creation inline with test logic              │
│  - Test data scattered across multiple test files           │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────┐
│                  Page/Request Classes                        │
│  ────────────────────────────────────────────────────────     │
│  - Accept parameters from test class                        │
│  - Create API payloads                                      │
│  - Make HTTP calls                                          │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────┐
│              APIBase / HTTP Client                           │
│  ────────────────────────────────────────────────────────     │
│  - Execute HTTP requests                                    │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────┐
│                   API Server                                 │
└──────────────────────────────────────────────────────────────┘
```

**Problems:**
- ❌ Test data hardcoded in test classes
- ❌ To change data, must modify test files
- ❌ No centralized data management
- ❌ Difficult to run different scenarios
- ❌ Not scalable for enterprise

### Improved Architecture (Solution)
```
┌──────────────────────────────────────────────────────────────┐
│                   Configuration Layer                        │
│  ────────────────────────────────────────────────────────     │
│  - test-config.yml (static configuration)                   │
│  - api-test-data.json (test datasets)                       │
│  - Environment mappings                                     │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────┐
│                  Data Access Layer                           │
│  ────────────────────────────────────────────────────────     │
│  - TestDataConfig (load YAML)                               │
│  - ApiTestDataProvider (TestNG integration)                 │
│  - DataGeneratorUtil (dynamic data)                         │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────┐
│              Business Logic Layer                            │
│  ────────────────────────────────────────────────────────     │
│  - Test Class (uses DataProvider)                           │
│  - Reads from configuration, not hardcoded                  │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────┐
│               API/Request Layer                              │
│  ────────────────────────────────────────────────────────     │
│  - Get parameters from test                                 │
│  - Build payloads dynamically                               │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────┐
│              HTTP Client / APIBase                           │
│  ────────────────────────────────────────────────────────     │
│  - Execute API calls                                        │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────┐
│                   API Server                                 │
└──────────────────────────────────────────────────────────────┘
```

**Benefits:**
- ✅ Test data centralized in JSON/YAML
- ✅ No test file modifications needed
- ✅ Add scenarios by updating JSON only
- ✅ Multiple scenarios per test
- ✅ Enterprise-scalable

---

## API Flow Patterns: Current vs Improved

### Pattern 1: KPI Data Flow

#### CURRENT (Problematic)
```java
// GetKpiLclUclValue.java - TEST CLASS
public class GetKpiLclUclValue extends APIBase {
    int definitionId = 9;              // ❌ Hardcoded
    int equipmentId = 4249;            // ❌ Hardcoded
    LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);  // ❌ Hardcoded
    LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0, 0, 0);    // ❌ Hardcoded
    int granularity = 60000;           // ❌ Hardcoded
    
    @Test
    public void getKpiLclUcl() throws IOException {
        // Get KPI metadata
        JsonNode kpiNode = GetKpiRequest.getKpiNode(getRequest(), definitionId, equipmentId);
        String lclUclType = kpiNode.path("lclUclType").asText(null);
        
        // Get KPI time series data
        Map<String, String> kpiData = GetKpiData.getKpiDataValue(
            machineId, kpiID, startTime, endTime, granularity
        );
        
        // Calculate statistics & verify
        // ...
    }
}

// GetKpiRequest.java - PAGE CLASS
public class GetKpiRequest {
    public static JsonNode getKpiNode(APIRequestContext request, int definitionId, int equipmentId) {
        String endpoint="/web/api/kpi-implementation?definition-id=" + definitionId + "&equipment-id=" + equipmentId;
        APIResponse response = request.get(endpoint);
        // Parse and return
    }
}

// GetKpiData.java - PAGE CLASS  
public class GetKpiData extends APIBase {
    public static String getKpiDataAPI(int machineID, int kpiID, DateRange dateRange, int granularity) {
        EquipKpi equipKpi = new EquipKpi(machineID, List.of(kpiID));
        EquipKpiRequest request = new EquipKpiRequest(List.of(equipKpi), dateRange, granularity, true);
        APIResponse response = postApiRequest(request, pathURL);
        return response.text();
    }
}
```

**Data Flow:**
```
Test Class (Hardcoded Data)
    ↓
GetKpiRequest.getKpiNode(definitionId, equipmentId)
    ↓
GetKpiData.getKpiDataValue(machineId, kpiID, startTime, endTime, granularity)
    ↓
Inline calculations & assertions
```

**Coupling Issues:**
- Test knows exact parameters (tight coupling)
- Changing parameters = modifying test file
- No test data reusability
- Difficult to scale

#### IMPROVED (Decoupled)
```java
// api-test-data.json - CONFIGURATION
{
  "kpiTestDataSets": [
    {
      "id": "kpi_lcl_ucl_set_1",
      "testName": "KPI LCL/UCL Calculation",
      "definitionId": 9,
      "equipmentId": 4249,
      "machineId": 4249,
      "kpiID": 9,
      "startDateTime": "2026-01-26T10:00:00",
      "endDateTime": "2026-01-27T10:00:00",
      "granularity": 60000
    }
  ]
}

// GetKpiLclUclValue.java - TEST CLASS (REFACTORED)
public class GetKpiLclUclValue extends APIBase {
    
    @DataProvider(name = "kpiLclUclData")
    public Object[][] provideTestData() {
        return ApiTestDataProvider.getKpiTestData();
    }
    
    @Test(dataProvider = "kpiLclUclData")
    public void getKpiLclUcl(ApiTestDataProvider.TestDataRecord record) throws IOException {
        // Get data from configuration
        int definitionId = record.getInt("definitionId");
        int equipmentId = record.getInt("equipmentId");
        int machineId = record.getInt("machineId");
        int kpiID = record.getInt("kpiID");
        LocalDateTime startTime = parseDateTime(record.getString("startDateTime"));
        LocalDateTime endTime = parseDateTime(record.getString("endDateTime"));
        int granularity = record.getInt("granularity");
        
        // API calls (same as before)
        JsonNode kpiNode = GetKpiRequest.getKpiNode(getRequest(), definitionId, equipmentId);
        String lclUclType = kpiNode.path("lclUclType").asText(null);
        
        Map<String, String> kpiData = GetKpiData.getKpiDataValue(
            machineId, kpiID, startTime, endTime, granularity
        );
        
        // Calculations & assertions (unchanged)
    }
}

// GetKpiRequest.java & GetKpiData.java - NO CHANGES NEEDED
// (Page classes remain same, just receive parameters)
```

**Data Flow:**
```
Configuration (api-test-data.json)
    ↓
ApiTestDataProvider (reads JSON)
    ↓
Test Class (uses TestDataRecord)
    ↓
GetKpiRequest.getKpiNode(definitionId, equipmentId)
    ↓
GetKpiData.getKpiDataValue(...)
    ↓
Inline calculations & assertions
```

**Decoupling Benefits:**
- Test doesn't know exact parameters (loose coupling)
- Changing parameters = updating JSON only
- Reusable across multiple scenarios
- Easy to scale

---

### Pattern 2: Raw Parameter Data Flow

#### CURRENT (Problem)
```java
public class GetRawParamLclUclValue extends APIBase {
    LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);  // ❌ Hardcoded
    LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0, 0, 0);    // ❌ Hardcoded
    int granularity = 60000;                                               // ❌ Hardcoded
    int plantId = 839;                                                     // ❌ Hardcoded
    int equipmentId = 4248;                                                // ❌ Hardcoded
    int rawParamDefId = 21;                                                // ❌ Hardcoded

    @Test
    public void getSTDMeanLCLUCL() throws IOException {
        JsonNode rawParamNode = GetRawParamRequest.getRawParamNode(
            getRequest(), plantId, equipmentId, rawParamDefId
        );
        
        Map<String, String> rawParameterData = GetRawParameterData.getRawParameterDataValue(
            equipmentId, rawParamDefId, startTime, endTime, granularity
        );
        
        // Process data
    }
}
```

#### IMPROVED
```java
public class GetRawParamLclUclValue extends APIBase {
    
    @DataProvider(name = "rawParamData")
    public Object[][] provideTestData() {
        return ApiTestDataProvider.getRawParameterTestData();
    }
    
    @Test(dataProvider = "rawParamData")
    public void getSTDMeanLCLUCL(ApiTestDataProvider.TestDataRecord record) throws IOException {
        int plantId = record.getInt("plantId");           // From config
        int equipmentId = record.getInt("equipmentId");   // From config
        int rawParamDefId = record.getInt("rawParamDefId");
        LocalDateTime startTime = parseDateTime(record.getString("startDateTime"));
        LocalDateTime endTime = parseDateTime(record.getString("endDateTime"));
        int granularity = record.getInt("granularity");
        
        // Rest of logic unchanged
        JsonNode rawParamNode = GetRawParamRequest.getRawParamNode(
            getRequest(), plantId, equipmentId, rawParamDefId
        );
        
        Map<String, String> rawParameterData = GetRawParameterData.getRawParameterDataValue(
            equipmentId, rawParamDefId, startTime, endTime, granularity
        );
    }
}
```

---

### Pattern 3: Aggregate Value Flow

#### CURRENT
```java
public class GetAggregateValueAndTypeOfKpi extends APIBase {
    int machineId = 4249;        // ❌ Hardcoded
    int kpiID = 9;               // ❌ Hardcoded
    // hardcoded startTime, endTime, etc.
    
    @Test
    public static void getKPIAggregateValue() throws JsonProcessingException {
        String startUtc = convertToUtc(startTime);
        String endUtc = convertToUtc(endTime);
        DateRange dateRange = new DateRange(startUtc, endUtc);
        
        String responseJson = getKpiAggregateDataFromApi(machineId, kpiID, dateRange);
        // Process response
    }
}
```

#### IMPROVED
```java
public class GetAggregateValueAndTypeOfKpi extends APIBase {
    
    @DataProvider(name = "aggregateData")
    public Object[][] provideTestData() {
        return ApiTestDataProvider.getAggregateTestData();
    }
    
    @Test(dataProvider = "aggregateData")
    public void getKPIAggregateValue(ApiTestDataProvider.TestDataRecord record) 
            throws JsonProcessingException {
        int machineId = record.getInt("machineId");
        int kpiID = record.getInt("kpiID");
        String startDateTime = record.getString("startDateTime");
        String endDateTime = record.getString("endDateTime");
        
        LocalDateTime startTime = parseDateTime(startDateTime);
        LocalDateTime endTime = parseDateTime(endDateTime);
        
        String startUtc = convertToUtc(startTime);
        String endUtc = convertToUtc(endTime);
        DateRange dateRange = new DateRange(startUtc, endUtc);
        
        String responseJson = getKpiAggregateDataFromApi(machineId, kpiID, dateRange);
    }
}
```

---

## Complete Refactoring: Step by Step

### Step 1: Analyze Current Test
```java
public class GetKpiData extends APIBase {
    // CURRENT - Identify these
    int machineID = 4249;           // What will become TEST DATA
    int kpiID = 9;                  // What will become TEST DATA
    LocalDateTime startTime = LocalDateTime.of(...);  // TEST DATA
    LocalDateTime endTime = LocalDateTime.of(...);    // TEST DATA
    int granularity = 60000;        // Could be config or data
    
    @Test
    public void myTest() {
        // Test logic
    }
}
```

### Step 2: Add to Configuration
```json
{
  "kpiTestDataSets": [
    {
      "id": "my_test_1",
      "testName": "My Test Scenario",
      "machineId": 4249,
      "kpiID": 9,
      "startDateTime": "2026-01-26T10:00:00",
      "endDateTime": "2026-01-27T10:00:00",
      "granularity": 60000
    }
  ]
}
```

### Step 3: Add imports and DataProvider
```java
import config.ApiTestDataProvider;
import org.testng.annotations.DataProvider;

public class GetKpiData extends APIBase {
    @DataProvider(name = "kpiData")
    public Object[][] provideTestData() {
        return ApiTestDataProvider.getKpiTestData();
    }
}
```

### Step 4: Update test signature and remove hardcoding
```java
// BEFORE
@Test
public void myTest() {
    int machineID = 4249;
    int kpiID = 9;
    // ...

// AFTER
@Test(dataProvider = "kpiData")
public void myTest(ApiTestDataProvider.TestDataRecord record) {
    int machineID = record.getInt("machineId");
    int kpiID = record.getInt("kpiID");
    String startDateTime = record.getString("startDateTime");
    // ...
}
```

### Step 5: Test and verify
```bash
mvn test -Dtest=GetKpiData
```

---

## Advanced: Dynamic Data Generation with APIs

### When to Use Dynamic Data
```
✅ DO USE when:
   - Test data changes frequently
   - Need unique IDs/timestamps per run
   - Running long test suites
   - Want realistic test scenarios

❌ DON'T USE when:
   - Test data is stable/fixed
   - Need specific known values
   - Comparing with historical data
   - Debugging specific scenarios
```

### Example: Hybrid Approach
```java
public class HybridApiTest extends APIBase {
    
    @DataProvider(name = "baseTestData")
    public Object[][] provideTestData() {
        return ApiTestDataProvider.getKpiTestData();
    }
    
    @Test(dataProvider = "baseTestData")
    public void testWithDynamicVariations(ApiTestDataProvider.TestDataRecord record) 
            throws IOException {
        // Use static test data from config
        int machineId = record.getInt("machineId");
        int kpiID = record.getInt("kpiID");
        
        // Generate variations dynamically
        List<Integer> equipmentVariations = DataGeneratorUtil.generateEquipmentIds(3, machineId);
        
        for (Integer equipmentId : equipmentVariations) {
            // Test with each variation
            testKpiWithEquipment(equipmentId, kpiID);
        }
    }
    
    private void testKpiWithEquipment(int equipmentId, int kpiID) throws IOException {
        // Test logic with dynamic variations
        System.out.println("Testing Equipment: " + equipmentId);
    }
}
```

---

## Environment-Specific API Flows

### Scenario: Test Against Multiple Environments

#### Configuration Management
```yaml
# test-config.yml
environments:
  sit:
    baseUrl: "https://sit-ipf.infinite-uptime.com"
    apiBaseUrl: "https://sit-ipf.infinite-uptime.com/query/api"
    timeout: 30000
    dbConnection: "sit-db"
    
  uat:
    baseUrl: "https://uat-new-ipf.infinite-uptime.com"
    apiBaseUrl: "https://uat-new-ipf.infinite-uptime.com/query/api"
    timeout: 30000
    dbConnection: "uat-db"
```

#### Test Implementation
```java
public class MultiEnvironmentApiTest extends EnterpriseApiBase {
    
    @Test
    public void testAcrossEnvironments() throws IOException {
        String currentEnv = getCurrentEnvironment();  // From system property
        String apiBaseUrl = getEnvironmentApiBaseUrl(currentEnv);
        
        System.out.println("Testing against: " + currentEnv);
        System.out.println("API Base URL: " + apiBaseUrl);
        
        // API calls automatically use correct environment
        // No test code changes needed!
    }
}
```

#### Execution
```bash
# Run against SIT
mvn test -Denvironment=sit

# Run against UAT
mvn test -Denvironment=uat

# Run against PROD
mvn test -Denvironment=prod
```

---

## Best Practices: API Flow Organization

### Recommended Structure
```
src/test/java/
├── base/
│   └── api/
│       ├── APIBase.java (unchanged)
│       └── EnterpriseApiBase.java (new - extends APIBase)
│
├── page/
│   └── api/
│       ├── GetKpiRequest.java (no changes needed)
│       ├── GetKpiData.java (no changes needed)
│       ├── GetRawParameterData.java (no changes needed)
│       └── /* All page classes unchanged */
│
└── test/
    └── api/
        ├── GetKpiLclUclValue.java (REFACTORED)
        ├── GetRawParamLclUclValue.java (REFACTORED)
        ├── GetAggregateValueAndTypeOfKpi.java (REFACTORED)
        └── /* Other tests - REFACTORED one by one */

src/test/resources/
└── testData/
    └── api/
        ├── test-config.yml (NEW)
        └── api-test-data.json (NEW/UPDATED)

src/main/java/
└── config/
    ├── TestDataConfig.java (NEW)
    ├── ApiTestDataProvider.java (NEW)
    └── DataGeneratorUtil.java (NEW)
```

### Key Principles
1. **Page classes don't change** - They accept parameters
2. **Test classes get refactored** - Add DataProvider, remove hardcoding
3. **Configuration centralized** - All test data in JSON/YAML
4. **No business logic in config** - Config only holds data
5. **Dynamic generation for variations** - DataGeneratorUtil for special cases

---

## Scaling: From 5 Tests to 100 Tests

### Month 1: Foundation
- Refactor 5-10 core API tests
- Test with 2-3 scenarios each
- Verify all pass

### Month 2: Expand
- Refactor remaining API tests
- Add environment-specific data
- Set up CI/CD integration

### Month 3: Scale
- Add UI test data management
- Implement database provider (optional)
- Create parameterized test suites
- Document all custom scenarios

### Month 4+: Maintain
- Add scenarios on-demand
- Monitor test performance
- Optimize data generation
- Scale to 100+ test combinations

---

## Common Refactoring Errors & Fixes

### Error 1: DateTime Parsing
```java
// ❌ WRONG
LocalDateTime dt = LocalDateTime.of(Integer.parseInt(record.getString("year")), ...);

// ✅ CORRECT
LocalDateTime dt = LocalDateTime.parse(
    record.getString("startDateTime"), 
    DateTimeFormatter.ISO_LOCAL_DATE_TIME
);
```

### Error 2: Type Casting
```java
// ❌ WRONG
int id = record.getInt("equipmentId");  // But JSON has it as string

// ✅ CORRECT
CheckJSON file - ensure numeric values don't have quotes
{
  "equipmentId": 4249  // NO quotes for numbers
}
```

### Error 3: Missing DataProvider
```java
// ❌ WRONG
@Test
public void testMethod(ApiTestDataProvider.TestDataRecord record) {
    // Forgot @DataProvider annotation!
}

// ✅ CORRECT
@DataProvider(name = "myData")
public Object[][] provideData() {
    return ApiTestDataProvider.getKpiTestData();
}

@Test(dataProvider = "myData")
public void testMethod(ApiTestDataProvider.TestDataRecord record) {
}
```

---

## Performance Optimization

### Data Loading
```java
// ✅ GOOD - Lazy load on first use
public static synchronized TestDataConfig getInstance() {
    if (instance == null) {
        instance = new TestDataConfig();
    }
    return instance;
}

// ❌ BAD - Reload on every test
TestDataConfig config = new TestDataConfig();  // Reloads JSON!
```

### Test Execution
```java
// ❌ SLOW - Parse datetime in each test
LocalDateTime dt = LocalDateTime.parse(record.getString("time"), 
    DateTimeFormatter.ISO_LOCAL_DATE_TIME);

// ✅ FAST - Store as string, parse once if needed
String dateTimeStr = record.getString("time");
// Use string in API calls, parse only if calculations needed
```

---

## Migration Checklist: All API Tests

- [ ] GetKpiLclUclValue
- [ ] GetRawParamLclUclValue  
- [ ] GetKpiData
- [ ] GetRawParameterData
- [ ] GetChartDataApi
- [ ] GetEquipment
- [ ] GetAggregateValueAndTypeOfKpi
- [ ] GetAggregateValueOfKpi
- [ ] ApiAggregateVerification
- [ ] All other API tests

**Effort per test:** ~30 minutes
**Total effort:** ~5 hours for full refactoring

---

## Summary: API Flow Transformation

### Before → After
| Aspect | Before | After |
|--------|--------|-------|
| Data Location | Scattered in test classes | Centralized JSON/YAML |
| To Add Scenario | Edit test code | Update JSON only |
| Test Variations | Manual test creation | DataProvider handles |
| Environment Switch | Code changes | Pass parameter |
| Maintainability | Difficult | Easy |
| Scalability | Limited | Enterprise |
| Code Duplication | High | None |
| Coupling | Tight | Loose |

---

**Next Step:** Pick your first API test and follow the Step-by-Step refactoring guide!
