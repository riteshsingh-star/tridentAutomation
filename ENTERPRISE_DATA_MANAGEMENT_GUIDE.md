# Enterprise Test Data Management Framework

## Overview
This document provides comprehensive guidelines for managing test data in an enterprise-grade automation framework, eliminating the need to modify JSON files or test classes for each test run.

## Architecture

### Three-Layer Data Management System

```
┌─────────────────────────────────────┐
│  Test Classes (Read-Only)           │
│  - No hardcoded test data           │
│  - Uses Data Providers              │
│  - Parameterized tests              │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  Data Access Layer                  │
│  - TestDataConfig (YAML-based)      │
│  - ApiTestDataProvider (JSON-based) │
│  - DataGeneratorUtil (Dynamic)      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  Configuration Files                │
│  - test-config.yml (Static config)  │
│  - api-test-data.json (Test data)   │
│  - config.properties (Credentials)  │
└─────────────────────────────────────┘
```

---

## 1. Configuration Files

### 1.1 test-config.yml (Static Configuration)
**Location:** `src/test/resources/testData/api/test-config.yml`

**Purpose:** 
- Centralized environment configuration
- API endpoint definitions
- Reusable test dataset templates
- Parameterized test scenarios

**Benefits:**
- Environment-agnostic test configuration
- Single source of truth for all test settings
- YAML format for easy reading and maintenance

**Example Usage:**
```yaml
environments:
  sit:
    baseUrl: "https://sit-ipf.infinite-uptime.com"
    timeout: 30000

testDataSets:
  kpiLclUcl:
    parameters:
      definitionId: 9
      equipmentId: 4249
    timeRange:
      startDate: "2026-01-26"
      startTime: "10:00:00"
```

### 1.2 api-test-data.json (Test Data Sets)
**Location:** `src/test/resources/testData/api/api-test-data.json`

**Purpose:**
- Test-specific data records
- Organized by test type (KPI, Raw Parameter, Aggregate)
- Includes expected results for assertions

**Benefits:**
- JSON format allows easy parsing
- Extensible structure for new test types
- Separates data from logic

**Example Usage:**
```json
{
  "kpiTestDataSets": [
    {
      "id": "kpi_lcl_ucl_set_1",
      "testName": "KPI LCL/UCL Calculation",
      "definitionId": 9,
      "equipmentId": 4249,
      "startDateTime": "2026-01-26T10:00:00",
      "endDateTime": "2026-01-27T10:00:00"
    }
  ]
}
```

---

## 2. Data Access Layer

### 2.1 TestDataConfig (Static Configuration Access)

**Usage in Tests:**
```java
@Test
public void testWithConfig() {
    TestDataConfig config = TestDataConfig.getInstance();
    
    // Get environment-specific config
    Map<String, Object> sitConfig = config.getEnvironmentConfig("sit");
    String baseUrl = (String) sitConfig.get("apiBaseUrl");
    
    // Get API endpoint
    String endpoint = config.getApiEndpoint("kpiTimeseries");
    
    // Get test dataset
    TestDataConfig.TestDataSet dataSet = config.getTestDataSet("kpiLclUcl");
    Integer equipmentId = (Integer) dataSet.getParameter("equipmentId");
}
```

**Key Methods:**
- `getInstance()` - Get singleton instance
- `getEnvironmentConfig(String environment)` - Get environment-specific config
- `getApiEndpoint(String endpointKey)` - Get API endpoint
- `getTestDataSet(String dataSetName)` - Get test dataset by name
- `getTestDataSetNames()` - Get all available test dataset names

### 2.2 ApiTestDataProvider (Data-Driven Testing)

**Usage in Tests with TestNG DataProvider:**
```java
public class GetKpiLclUclValue extends APIBase {
    
    @DataProvider(name = "kpiTestData")
    public Object[][] getTestData() {
        return ApiTestDataProvider.getKpiTestData();
    }
    
    @Test(dataProvider = "kpiTestData")
    public void testKpiLclUcl(ApiTestDataProvider.TestDataRecord record) {
        int definitionId = record.getInt("definitionId");
        int equipmentId = record.getInt("equipmentId");
        String startTime = record.getString("startDateTime");
        
        // Test logic here
        Assert.assertNotNull(record.get("expectedResults"));
    }
}
```

**Key Methods:**
- `getKpiTestData()` - Returns 2D array for TestNG DataProvider
- `getRawParameterTestData()` - Raw parameter test data
- `getAggregateTestData()` - Aggregate test data
- `getTestDataById(String dataSetType, String testId)` - Get specific test data

**TestDataRecord Methods:**
- `getString(String key)` - Get string value
- `getInt(String key)` - Get integer value
- `getLong(String key)` - Get long value
- `getDouble(String key)` - Get double value
- `getBoolean(String key)` - Get boolean value
- `getMap(String key)` - Get nested map
- `get(String key)` - Get any value

### 2.3 DataGeneratorUtil (Dynamic Test Data)

**Purpose:** Generate unique, non-hardcoded test data for each test run

**No need to modify JSON files** - the framework generates dynamic data on-the-fly!

**Usage in Tests:**
```java
public class DynamicTestExample extends APIBase {
    
    @Test
    public void testWithDynamicData() throws IOException {
        // Generate unique identifiers
        String uniqueTestId = DataGeneratorUtil.generateUniqueId("KPI_TEST");
        String uniqueName = DataGeneratorUtil.generateUniqueTestName("Dashboard");
        
        // Generate date range relative to current time (24 hours back)
        Map<String, String> dateRange = DataGeneratorUtil.generateDateRange(24, 0);
        String startTime = dateRange.get("start");
        String endTime = dateRange.get("end");
        
        // Generate test scenario on-the-fly
        List<Integer> equipmentIds = DataGeneratorUtil.generateEquipmentIds(3, 4248);
        List<Integer> kpiIds = DataGeneratorUtil.generateKpiIds(2, 9);
        Map<String, Object> scenario = DataGeneratorUtil.generateTestScenario(
            "Multi-Equipment KPI Test",
            equipmentIds,
            kpiIds,
            2880
        );
        
        // Use generated data in test
        System.out.println("Test ID: " + uniqueTestId);
        System.out.println("Started at: " + startTime);
        System.out.println("Equipment IDs: " + equipmentIds);
    }
}
```

**Key Methods:**
- `generateUniqueId(String prefix)` - Generate unique ID with timestamp
- `generateUniqueTestName(String baseName)` - Generate unique test name
- `generateDateRange(int hoursBack, int hoursForward)` - Generate relative date range
- `generateDateRange(LocalDateTime start, LocalDateTime end)` - Generate specific date range
- `generateEquipmentIds(int count, int startId)` - Generate list of equipment IDs
- `generateKpiIds(int count, int startId)` - Generate list of KPI IDs
- `generateTestScenario(...)` - Generate complete test scenario

---

## 3. Migration Guide: From Hardcoded to Config-Driven

### Before (Hardcoded - NOT Recommended)
```java
public class GetKpiLclUclValue extends APIBase {
    int definitionId = 9;
    int equipmentId = 4249;
    LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);
    LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0, 0, 0);
    
    @Test
    public void getKpiLclUcl() throws IOException {
        // Test logic
    }
}
```
**Problems:**
- Data hardcoded in test class
- Must modify test file for each new dataset
- Not scalable
- Difficult to maintain

### After (Config-Driven - RECOMMENDED)

**Option 1: Using TestDataConfig with Static Configuration**
```java
public class GetKpiLclUclValue extends APIBase {
    
    @Test
    public void getKpiLclUcl() throws IOException {
        TestDataConfig config = TestDataConfig.getInstance();
        TestDataConfig.TestDataSet dataSet = config.getTestDataSet("kpiLclUcl");
        
        int definitionId = ((Number) dataSet.getParameter("definitionId")).intValue();
        int equipmentId = ((Number) dataSet.getParameter("equipmentId")).intValue();
        Map<String, Object> timeRange = dataSet.getTimeRange();
        
        LocalDateTime startTime = LocalDateTime.of(
            Integer.parseInt((String) timeRange.get("startDate").replace("-", "")),
            Integer.parseInt((String) timeRange.get("startTime").substring(0, 2)),
            Integer.parseInt((String) timeRange.get("startTime").substring(3, 5)),
            0
        );
        
        // Test logic using centralized config
    }
}
```

**Option 2: Using ApiTestDataProvider with TestNG DataProvider (BEST)**
```java
public class GetKpiLclUclValue extends APIBase {
    
    @DataProvider(name = "kpiTestData")
    public Object[][] getTestData() {
        return ApiTestDataProvider.getKpiTestData();
    }
    
    @Test(dataProvider = "kpiTestData")
    public void getKpiLclUcl(ApiTestDataProvider.TestDataRecord record) throws IOException {
        int definitionId = record.getInt("definitionId");
        int equipmentId = record.getInt("equipmentId");
        String startTime = record.getString("startDateTime");
        
        // Test logic - clean and simple!
    }
}
```

**Option 3: Using DataGeneratorUtil for Dynamic Data (MOST FLEXIBLE)**
```java
public class GetKpiLclUclValue extends APIBase {
    
    @Test
    public void getKpiLclUcl() throws IOException {
        // No hardcoded data - dynamically generated per run!
        Map<String, String> dateRange = DataGeneratorUtil.generateDateRange(24, 0);
        String uniqueTestId = DataGeneratorUtil.generateUniqueId("KPI_LCL_UCL");
        
        int definitionId = 9;
        int equipmentId = 4249;
        String startTime = dateRange.get("start");
        String endTime = dateRange.get("end");
        
        // Test logic with fresh data each time
    }
}
```

---

## 4. Best Practices

### 4.1 DO

✅ **DO** Use TestDataConfig for environment-specific settings
```java
String baseUrl = TestDataConfig.getInstance()
    .getEnvironmentConfig("sit")
    .get("apiBaseUrl");
```

✅ **DO** Use ApiTestDataProvider for data-driven tests with TestNG
```java
@Test(dataProvider = "kpiTestData")
public void testKpi(ApiTestDataProvider.TestDataRecord record) {
    // Multiple test cases from single test method
}
```

✅ **DO** Use DataGeneratorUtil for dynamic, fresh data
```java
Map<String, String> freshDateRange = DataGeneratorUtil.generateDateRange(24, 0);
```

✅ **DO** Keep test data organized in separate JSON/YAML files
```
src/test/resources/testData/
├── api/
│   ├── test-config.yml
│   └── api-test-data.json
└── ui/
    └── ui-test-data.json
```

✅ **DO** Use centralized configuration for API endpoints
```java
String endpoint = TestDataConfig.getInstance().getApiEndpoint("kpiTimeseries");
```

### 4.2 DON'T

❌ **DON'T** Hardcode test data in test classes
```java
// ❌ BAD
LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);
int equipmentId = 4249;
```

❌ **DON'T** Modify JSON files between test runs
```
// ❌ BAD - Creating new files each run defeats the purpose
api-test-data-run1.json
api-test-data-run2.json
api-test-data-run3.json
```

❌ **DON'T** Mix test data with test logic
```java
// ❌ BAD
public Map<String, Object> createPayload() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("equipmentId", 4249);  // Should be in config
    payload.put("kpiId", 9);            // Should be in config
    return payload;
}
```

❌ **DON'T** Use environment-specific test methods
```java
// ❌ BAD - Creates code duplication
public void testSit() { ... }
public void testUat() { ... }
public void testProd() { ... }

// ✅ GOOD - Single test using environment-agnostic config
public void testAllEnvironments() { ... }
```

---

## 5. Scaling the Framework

### 5.1 Add New Test Data Sets

**Step 1:** Add to `api-test-data.json`
```json
{
  "id": "my_new_test",
  "testName": "My New Test Scenario",
  "equipmentId": 4250,
  "kpiID": 10,
  "startDateTime": "2026-02-01T10:00:00",
  "endDateTime": "2026-02-02T10:00:00"
}
```

**Step 2:** Use in test with DataProvider
```java
@DataProvider(name = "myNewTestData")
public Object[][] getNewTestData() {
    return ApiTestDataProvider.getKpiTestData(); // Automatically picks up new data
}

@Test(dataProvider = "myNewTestData")
public void testNewScenario(ApiTestDataProvider.TestDataRecord record) {
    // Framework automatically runs test with all datasets!
}
```

### 5.2 Add New Environments

**Step 1:** Update `test-config.yml`
```yaml
environments:
  prod:
    baseUrl: "https://prod-ipf.infinite-uptime.com"
    apiBaseUrl: "https://prod-ipf.infinite-uptime.com"
    timeout: 30000
```

**Step 2:** Use in test
```java
String environment = System.getProperty("environment", "sit");
TestDataConfig config = TestDataConfig.getInstance();
Map<String, Object> envConfig = config.getEnvironmentConfig(environment);
```

**Step 3:** Run with environment parameter
```bash
mvn test -Denvironment=prod
```

### 5.3 Create Parameterized Test Scenarios

**Step 1:** Add scenarios to `test-config.yml`
```yaml
parameterizedTestData:
  kpiScenarios:
    - id: "scenario_1"
      name: "Single Equipment Single KPI"
      equipmentIds: [4249]
      kpiIds: [9]
    - id: "scenario_2"
      name: "Multiple Equipment"
      equipmentIds: [4248, 4249, 4250]
      kpiIds: [9, 10, 11]
```

**Step 2:** Use with TestNG parametrization
```java
@DataProvider(name = "kpiScenarios")
public Object[][] getKpiScenarios() {
    TestDataConfig config = TestDataConfig.getInstance();
    List<Map<String, Object>> scenarios = config.getTestScenarios("kpiScenarios");
    Object[][] data = new Object[scenarios.size()][1];
    for (int i = 0; i < scenarios.size(); i++) {
        data[i][0] = scenarios.get(i);
    }
    return data;
}

@Test(dataProvider = "kpiScenarios")
public void testKpiScenarios(Map<String, Object> scenario) {
    System.out.println("Running: " + scenario.get("name"));
    // Test uses multiple scenarios without code changes!
}
```

---

## 6. Advanced Scenarios

### 6.1 Data-Driven with Dynamic Generation
```java
@Test
public void testWithDynamicData() {
    // Use static config for baseline
    TestDataConfig config = TestDataConfig.getInstance();
    int baseEquipmentId = ((Number) 
        config.getTestDataSet("kpiLclUcl")
        .getParameter("equipmentId")).intValue();
    
    // Generate variations dynamically
    List<Integer> equipmentIds = DataGeneratorUtil.generateEquipmentIds(5, baseEquipmentId);
    
    for (Integer equipmentId : equipmentIds) {
        // Test with each variation
        verifyKpiData(equipmentId);
    }
}
```

### 6.2 Database-Driven Test Data (Future Enhancement)
```java
// Framework ready for database integration!
public class DatabaseTestDataProvider {
    public static List<TestDataRecord> getTestDataFromDatabase(String query) {
        // Implementation can fetch data from database
        // No code changes needed - just implement this provider
    }
}
```

### 6.3 API-Driven Test Data (Future Enhancement)
```java
// Framework supports fetching test data from external APIs
public class RemoteTestDataProvider {
    public static List<TestDataRecord> getTestDataFromApi(String apiUrl) {
        // Implementation fetches data from external system
        // Keeps tests updated with latest environment data
    }
}
```

---

## 7. Configuration Management

### 7.1 Environment-Specific Configuration

Run tests against different environments without code changes:

```bash
# Run against SIT
mvn test -Denvironment=sit

# Run against UAT
mvn test -Denvironment=uat

# Run against PROD
mvn test -Denvironment=prod
```

### 7.2 Create Environment Overrides

**Optional:** Create environment-specific test-data files
```
src/test/resources/testData/api/
├── api-test-data.json (base)
├── api-test-data-sit.json (SIT overrides)
├── api-test-data-uat.json (UAT overrides)
└── api-test-data-prod.json (PROD overrides)
```

---

## 8. Reporting & Logging

### All Tests Report Which Data Was Used

```java
@Test(dataProvider = "kpiTestData")
public void testKpi(ApiTestDataProvider.TestDataRecord record) {
    String testName = record.getString("testName");
    String testId = record.getString("id");
    
    // Automatically logged and reported
    System.out.println("Running test: " + testName);
    System.out.println("Test ID: " + testId);
    
    // Reports show which exact data scenario was tested
}
```

---

## 9. Summary: Key Benefits

| Aspect | Before | After |
|--------|--------|-------|
| **Data Management** | Hardcoded in test classes | Centralized configuration files |
| **Test Modifications per Run** | Required | Not required |
| **New Test Scenario** | Modify code | Add JSON entry |
| **Environment Switching** | Modify test code | Pass parameter |
| **Scalability** | Limited | Enterprise-ready |
| **Maintainability** | Difficult | Easy |
| **Reusability** | Low | High |
| **Test Variations** | Code duplication | DataProvider handles |
| **Configuration Management** | Scattered | Centralized |
| **CI/CD Integration** | Complex | Simple |

---

## 10. Quick Reference

### Load Test Configuration
```java
TestDataConfig config = TestDataConfig.getInstance();
```

### Get Test Data as DataProvider
```java
@DataProvider(name = "testData")
public Object[][] getTestData() {
    return ApiTestDataProvider.getKpiTestData();
}
```

### Generate Dynamic Data
```java
String uniqueId = DataGeneratorUtil.generateUniqueId("TEST");
Map<String, String> dateRange = DataGeneratorUtil.generateDateRange(24, 0);
```

### Access Test Record Values
```java
ApiTestDataProvider.TestDataRecord record = ...;
int equipmentId = record.getInt("equipmentId");
String startTime = record.getString("startDateTime");
```

---

## Implementation Checklist

- [x] Create `test-config.yml` for static configuration
- [x] Create `api-test-data.json` for test datasetss
- [x] Implement `TestDataConfig` for configuration access
- [x] Implement `ApiTestDataProvider` for data-driven testing
- [x] Implement `DataGeneratorUtil` for dynamic data generation
- [ ] Update all API test classes to use configuration
- [ ] Remove hardcoded test data from test classes
- [ ] Add new test scenarios to JSON files
- [ ] Document test data additions in team wiki
- [ ] Set up CI/CD pipeline with parameterized environment

---

**Next Steps:**
1. Review [api-test-data.json](api-test-data.json) for existing test cases
2. Review [test-config.yml](test-config.yml) for configuration structure
3. Refactor one existing test class to use the new approach
4. Document your custom test data scenarios
5. Deploy to CI/CD pipeline
