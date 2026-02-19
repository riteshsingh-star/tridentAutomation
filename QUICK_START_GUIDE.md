# Quick Start: 5-Minute Setup Guide

## Overview
Transform your Playwright Java framework from hardcoded test data to enterprise-grade centralized management.

---

## What You Get

✅ **Centralized Test Data Management**
- No more JSON file modifications between runs
- Single source of truth for all test configuration
- Environment-agnostic tests

✅ **Data-Driven Testing**
- One test method, multiple scenarios
- Automatic test execution for each dataset
- TestNG DataProvider integration

✅ **Dynamic Data Generation**
- Generate unique test data per run
- Fresh timestamps and IDs automatically
- No static data hardcoding

✅ **Multi-Environment Support**
- Run same tests against SIT, UAT, PROD
- Just change a parameter
- No code modifications needed

---

## Files Added

### Configuration Files
```
src/test/resources/testData/api/
├── test-config.yml          # Environment & endpoint configuration
└── api-test-data.json        # Test datasets (KPI, Raw Parameter, Aggregate)
```

### Utility Classes
```
src/main/java/config/
├── TestDataConfig.java       # Load and access YAML configuration
├── ApiTestDataProvider.java  # TestNG DataProvider implementation
└── DataGeneratorUtil.java    # Generate dynamic test data

src/main/java/base/api/
└── EnterpriseApiBase.java    # Enhanced base class for data management
```

### Example Tests (Reference)
```
src/test/java/test/api/
├── GetKpiLclUclValueRefactored.java          # Before/After example
└── GetRawParamLclUclValueRefactored.java     # Before/After example
```

### Documentation
```
├── ENTERPRISE_DATA_MANAGEMENT_GUIDE.md    # Comprehensive guide
└── IMPLEMENTATION_ROADMAP.md               # Step-by-step roadmap
```

---

## Quick Start: 3 Options

### Option 1: Use Static Configuration (Easiest)

**When to use:** You have fixed test data that doesn't change much

```java
@Test
public void testWithStaticConfig() {
    TestDataConfig config = TestDataConfig.getInstance();
    TestDataConfig.TestDataSet dataSet = config.getTestDataSet("kpiLclUcl");
    
    int equipmentId = ((Number) dataSet.getParameter("equipmentId")).intValue();
    // Use equipmentId in test
}
```

**Pros:** Simple, fast, minimal code
**Cons:** Still have to load config in every test

---

### Option 2: Use DataProvider (Recommended)

**When to use:** You have multiple test scenarios or want data-driven tests

```java
@DataProvider(name = "kpiTestData")
public Object[][] getTestData() {
    return ApiTestDataProvider.getKpiTestData();
}

@Test(dataProvider = "kpiTestData")
public void testKpi(ApiTestDataProvider.TestDataRecord record) {
    int equipmentId = record.getInt("equipmentId");
    String startTime = record.getString("startDateTime");
    // Test logic
}
```

**Pros:** 
- Multiple test scenarios automatically
- Clean test method code
- Add new data without modifying test class
- TestNG handles iteration

**Cons:** Need to structure test data in JSON properly

---

### Option 3: Use Dynamic Generation (Most Flexible)

**When to use:** Test data changes frequently or needs to be unique per run

```java
@Test
public void testWithDynamicData() {
    String uniqueId = DataGeneratorUtil.generateUniqueId("TEST");
    Map<String, String> dateRange = DataGeneratorUtil.generateDateRange(24, 0);
    List<Integer> equipmentIds = DataGeneratorUtil.generateEquipmentIds(3, 4248);
    
    // Test with fresh data every run
}
```

**Pros:**
- Unique data every run
- No file modifications needed
- Flexible parameter generation

**Cons:** Still need to know base values

---

## Real Example: Before & After

### BEFORE (Hardcoded - Current)
```java
public class GetKpiLclUclValue extends APIBase {
    int definitionId = 9;
    int equipmentId = 4249;
    LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);
    LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0, 0, 0);
    int granularity = 60000;

    @Test
    public void getKpiLclUcl() throws IOException {
        // Test logic
    }
}
```

**Problems:**
- Must edit this file to change test data ❌
- Different data sets = different test files ❌
- Hard to scale ❌

### AFTER (Config-Driven - New)
```java
public class GetKpiLclUclValue extends APIBase {
    
    @DataProvider(name = "kpiLclUclDataProvider")
    public Object[][] provideTestData() {
        return ApiTestDataProvider.getKpiTestData();
    }

    @Test(dataProvider = "kpiLclUclDataProvider")
    public void getKpiLclUcl(ApiTestDataProvider.TestDataRecord record) throws IOException {
        int definitionId = record.getInt("definitionId");
        int equipmentId = record.getInt("equipmentId");
        String startDateTime = record.getString("startDateTime");
        // Test logic
    }
}
```

**Benefits:**
- No hardcoded data ✅
- Add scenarios in JSON only ✅
- Scales easily ✅
- Enterprise-ready ✅

---

## Step 1: Add Test Data (2 minutes)

**File:** `src/test/resources/testData/api/api-test-data.json`

Add your test case to the appropriate array:

```json
{
  "kpiTestDataSets": [
    {
      "id": "kpi_test_1",
      "testName": "My First Test",
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
```

---

## Step 2: Update Your Test (5 minutes)

**Find:** Your test class (e.g., `GetKpiLclUclValue.java`)

**Add imports:**
```java
import config.ApiTestDataProvider;
import org.testng.annotations.DataProvider;
```

**Add DataProvider method:**
```java
@DataProvider(name = "kpiLclUclDataProvider")
public Object[][] provideTestData() {
    return ApiTestDataProvider.getKpiTestData();
}
```

**Update test method:**
```java
@Test(dataProvider = "kpiLclUclDataProvider")
public void getKpiLclUcl(ApiTestDataProvider.TestDataRecord record) throws IOException {
    // Extract data from record
    int definitionId = record.getInt("definitionId");
    int equipmentId = record.getInt("equipmentId");
    String startDateTime = record.getString("startDateTime");
    
    // Rest of test logic unchanged
}
```

**That's it!** Your test now runs with centralized data.

---

## Step 3: Run Tests (1 minute)

### Run specific test
```bash
mvn test -Dtest=GetKpiLclUclValue
```

### Run against different environment
```bash
mvn test -Dtest=GetKpiLclUclValue -Denvironment=uat
```

### Run with all datasets
```bash
# If you have multiple entries in api-test-data.json,
# test automatically runs for each one
mvn test -Dtest=GetKpiLclUclValue
```

---

## Key Concepts Quick Reference

### TestDataConfig - Access Static Configuration
```java
// Get environment config
Map<String, Object> sitConfig = TestDataConfig.getInstance()
    .getEnvironmentConfig("sit");

// Get API endpoint
String endpoint = TestDataConfig.getInstance()
    .getApiEndpoint("kpiTimeseries");

// Get test dataset
TestDataConfig.TestDataSet dataSet = TestDataConfig.getInstance()
    .getTestDataSet("kpiLclUcl");

// Get parameter value
Integer equipmentId = (Integer) dataSet.getParameter("equipmentId");
```

### ApiTestDataProvider - Data-Driven Testing
```java
// Use in DataProvider
@DataProvider(name = "testData")
public Object[][] getData() {
    return ApiTestDataProvider.getKpiTestData();
}

// Use in test method
@Test(dataProvider = "testData")
public void test(ApiTestDataProvider.TestDataRecord record) {
    int id = record.getInt("equipmentId");
    String name = record.getString("testName");
    Map<String, Object> expectedResults = record.getMap("expectedResults");
}
```

### DataGeneratorUtil - Dynamic Data
```java
// Generate unique ID
String uniqueId = DataGeneratorUtil.generateUniqueId("TEST");  
// Result: TEST_20260117101530_1

// Generate date range (relative)
Map<String, String> dateRange = DataGeneratorUtil.generateDateRange(24, 0);
// Result: {start: 2026-01-16T10:15:30, end: 2026-01-17T10:15:30}

// Generate IDs list
List<Integer> ids = DataGeneratorUtil.generateEquipmentIds(3, 4248);
// Result: [4248, 4249, 4250]

// Get current timestamp
String now = DataGeneratorUtil.getCurrentTimestamp();
```

---

## Adding More Test Scenarios (Super Easy!)

**No code changes needed!**

Just add to `api-test-data.json`:

```json
{
  "id": "kpi_test_2",
  "testName": "Another Scenario",
  "definitionId": 10,
  "equipmentId": 4250,
  "machineId": 4250,
  "kpiID": 10,
  "startDateTime": "2026-02-01T08:00:00",
  "endDateTime": "2026-02-02T08:00:00",
  "granularity": 60000
}
```

**Run test - it automatically picks up the new scenario!**
```bash
mvn test -Dtest=GetKpiLclUclValue
# Now runs with BOTH kpi_test_1 AND kpi_test_2
```

---

## Environment Variables

**Pass via command line or environment:**

```bash
# Set environment
mvn test -Denvironment=prod

# Set multiple properties
mvn test -Dtest=GetKpiLclUclValue -Denvironment=uat -Dlog.level=DEBUG

# In CI/CD (Jenkins)
mvn test -Denvironment=${ENVIRONMENT} -Dbrowser=${BROWSER}
```

---

## Common Questions

**Q: Do I need to modify JSON files after each test run?**
A: No! That's the whole point. Framework generates unique data automatically with DataGeneratorUtil.

**Q: Can I have different test data for SIT vs UAT?**
A: Yes. Add environment-specific JSON files and implement loading logic. See ENTERPRISE_DATA_MANAGEMENT_GUIDE.md

**Q: What if my test data is in a database?**
A: Create a DatabaseTestDataProvider class (framework is ready for this extension).

**Q: How do I debug test data issues?**
A: Print the record: `System.out.println(record.toString());`

**Q: Can I mix hardcoded and config-driven data?**
A: Yes, but not recommended. Better to move all to config for consistency.

---

## Execution Flow

```
Test Class
    ↓
@Test(dataProvider = "kpiTestData")
    ↓
ApiTestDataProvider.getKpiTestData()
    ↓
Reads api-test-data.json
    ↓
Creates TestDataRecord objects
    ↓
For EACH entry in kpiTestDataSets:
    └─→ Executes test() with that record
    └─→ Test uses record.getInt(), record.getString(), etc.
    └─→ Report shows which dataset was used
```

---

## Files You'll Modify

### Phase 1 (Immediate)
- ✏️ `src/test/resources/testData/api/api-test-data.json` - Add your test data
- ✏️ Individual test classes - Add DataProvider, update test signature

### Phase 2 (Optional)
- ✏️ `src/test/resources/testData/api/test-config.yml` - Add environments, endpoints
- ✏️ Create environment-specific JSON files

### Files You Won't Touch
- ❌ Configuration utilities - They're production code now
- ❌ EnterpriseApiBase - Extend it, don't modify it
- ❌ DataGeneratorUtil - Use it, don't modify it

---

## Success Criteria

After implementing, you should be able to:

✅ Add new test scenario without modifying test code
✅ Run same test against multiple environments
✅ Generate unique test IDs/timestamps automatically
✅ Access any configuration value through `TestDataConfig`
✅ Use DataProvider to run multiple test scenarios from one test method
✅ Delete a test scenario by removing JSON entry (no code touch)
✅ Brief team members by showing the JSON files (not scary code!)

---

## Next Steps

1. **Review** the setup files created in your project
2. **Pick one test class** to refactor as pilot
3. **Follow Step 1-3** in this guide
4. **Run the test** to verify it passes
5. **Add another scenario** to JSON
6. **See it run automatically**
7. **Wow moments happen!** ✨

---

## Support Resources

📄 **Files to Review:**
- [API Test Data](src/test/resources/testData/api/api-test-data.json)
- [Test Configuration](src/test/resources/testData/api/test-config.yml)
- [TestDataConfig](src/main/java/config/TestDataConfig.java)
- [DataGeneratorUtil](src/main/java/config/DataGeneratorUtil.java)

📖 **Documentation:**
- [Enterprise Management Guide](ENTERPRISE_DATA_MANAGEMENT_GUIDE.md)
- [Implementation Roadmap](IMPLEMENTATION_ROADMAP.md)

🎯 **Reference Examples:**
- [Refactored KPI Test](src/test/java/test/api/GetKpiLclUclValueRefactored.java)
- [Refactored Raw Param Test](src/test/java/test/api/GetRawParamLclUclValueRefactored.java)

---

## Let's Go! 🚀

Your enterprise-ready test automation framework awaits. Start with one test, scale to all tests, profit! 

Questions? Check the **ENTERPRISE_DATA_MANAGEMENT_GUIDE.md** for comprehensive documentation.
