# Enterprise Framework Implementation Roadmap

## Phase 1: Foundation (Week 1-2) ✓ Completed
- [x] Create centralized test data configuration files
  - [x] `test-config.yml` - Static environment and test configuration
  - [x] `api-test-data.json` - Test datasets organized by test type
- [x] Implement data access utilities
  - [x] `TestDataConfig.java` - Configuration management
  - [x] `ApiTestDataProvider.java` - TestNG data provider
  - [x] `DataGeneratorUtil.java` - Dynamic data generation
- [x] Create enterprise base classes
  - [x] `EnterpriseApiBase.java` - Enhanced API test base
- [x] Documentation
  - [x] `ENTERPRISE_DATA_MANAGEMENT_GUIDE.md` - Comprehensive guide

---

## Phase 2: Migration (Week 2-3) - NEXT STEPS
### Refactor Existing API Tests

#### Step 1: Update GetKpiLclUclValue Test
**File:** [src/test/java/test/api/GetKpiLclUclValue.java](src/test/java/test/api/GetKpiLclUclValue.java)

**Reference:** [GetKpiLclUclValueRefactored.java](src/test/java/test/api/GetKpiLclUclValueRefactored.java)

**Changes Required:**
```java
// BEFORE - Remove hardcoded data
int definitionId = 9;
int equipmentId = 4249;
LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);
LocalDateTime endTime = LocalDateTime.of(2026, 1, 27, 10, 0, 0, 0);

// AFTER - Use DataProvider
@DataProvider(name = "kpiLclUclDataProvider")
public Object[][] provideTestData() {
    return ApiTestDataProvider.getKpiTestData();
}

@Test(dataProvider = "kpiLclUclDataProvider")
public void getKpiLclUcl(ApiTestDataProvider.TestDataRecord record) throws IOException {
    int definitionId = record.getInt("definitionId");
    //... rest of test
}
```

**Time Estimate:** 30 minutes

#### Step 2: Update GetRawParamLclUclValue Test
**File:** [src/test/java/test/api/GetRawParamLclUclValue.java](src/test/java/test/api/GetRawParamLclUclValue.java)

**Reference:** [GetRawParamLclUclValueRefactored.java](src/test/java/test/api/GetRawParamLclUclValueRefactored.java)

**Time Estimate:** 30 minutes

#### Step 3: Update GetKpiData Test
**File:** [src/test/java/test/api/GetKpiData.java](src/test/java/test/api/GetKpiData.java)

**Time Estimate:** 30 minutes

#### Step 4: Update GetRawParameterData Test
**File:** [src/test/java/test/api/GetRawParameterData.java](src/test/java/test/api/GetRawParameterData.java)

**Time Estimate:** 30 minutes

#### Step 5: Update Aggregate Tests
**Files:**
- [src/test/java/test/api/GetAggregateValueAndTypeOfKpi.java](src/test/java/test/api/GetAggregateValueAndTypeOfKpi.java)
- [src/test/java/test/api/GetAggregateValueOfKpi.java](src/test/java/test/api/GetAggregateValueOfKpi.java)

**Time Estimate:** 30 minutes each

**Total Phase 2 Time:** ~2.5-3 hours of refactoring

---

## Phase 3: UI Test Migration (Week 3-4)
### Move Web Test Data to Configuration

#### Files to Migrate:
1. **DashboardTest.java** - Update to use TestDataConfig
2. **AdminFlowTest.java** - Update to use TestDataConfig
3. **CreateUserAndDeviceTest.java** - Update to use TestDataConfig
4. **EquipmentDataSetup.java** - Update to use TestDataConfig

#### Actions:
- Create `ui-test-data.json` (similar structure to API tests)
- Create `UiTestDataProvider.java` (similar to ApiTestDataProvider)
- Migrate JSON files from root testData/ to organized structure
- Update test classes to use DataProvider

---

## Phase 4: Advanced Features (Week 4+)
### Optional Enhancements for Enterprise Scale

#### 4.1 Database-Driven Test Data
```java
public class DatabaseTestDataProvider {
    public static List<TestDataRecord> getTestDataFromDb(String query) {
        // Implementation to fetch test data from database
        // Allows dynamic test data management
    }
}
```

#### 4.2 Externalized Configuration Server
```java
public class ConfigServerProvider {
    public static Map<String, Object> getConfigFromServer(String url) {
        // Fetch configuration from remote server
        // Enables zero-copy deployments
    }
}
```

#### 4.3 Test Environment Profiles
Create environment-specific overrides:
```
testData/
├── api/
│   ├── api-test-data.json (base)
│   ├── api-test-data-sit.json (SIT overrides)
│   ├── api-test-data-uat.json (UAT overrides)
│   └── api-test-data-prod.json (PROD overrides)
```

#### 4.4 Test Data Versioning
```java
public class TestDataVersion {
    public static final String VERSION = "2.0";
    public static final String SIT_VERSION = "2.0.1";
    public static final String UAT_VERSION = "2.0";
}
```

---

## Migration Template: Step-by-Step Example

### Template for Refactoring Any API Test

**Step 1: Replace imports**
```java
// Add
import config.ApiTestDataProvider;
import org.testng.annotations.DataProvider;

// Keep
import test specific imports you already use
```

**Step 2: Add DataProvider method**
```java
@DataProvider(name = "testDataProvider")
public Object[][] provideTestData() {
    return ApiTestDataProvider.getKpiTestData();  // or appropriate method
}
```

**Step 3: Update @Test annotation**
```java
// Before
@Test
public void myTest() throws IOException {

// After
@Test(dataProvider = "testDataProvider")
public void myTest(ApiTestDataProvider.TestDataRecord record) throws IOException {
```

**Step 4: Replace hardcoded variables**
```java
// Before
int equipmentId = 4249;
LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 10, 0, 0, 0);

// After
int equipmentId = record.getInt("equipmentId");
LocalDateTime startTime = LocalDateTime.parse(record.getString("startDateTime"), 
                                             DateTimeFormatter.ISO_LOCAL_DATE_TIME);
```

**Step 5: Test and verify**
```bash
# Run the refactored test
mvn test -Dtest=GetKpiLclUclValue

# Run with specific environment
mvn test -Dtest=GetKpiLclUclValue -Denvironment=uat

# Run all API tests
mvn test -Dgroups=API
```

---

## Data Structure Reference

### Current Configuration Hierarchy
```
TestDataConfig (Singleton)
│
├── Environments (from test-config.yml)
│   ├── sit
│   ├── uat
│   └── prod
│
├── API Endpoints (from test-config.yml)
│   ├── kpiTimeseries
│   ├── rawParameterTimeseries
│   └── ...
│
├── Test Data Sets (from test-config.yml)
│   ├── kpiLclUcl
│   ├── rawParameterLclUcl
│   └── ...
│
└── Parameterized Test Scenarios (from test-config.yml)
    ├── kpiScenarios
    └── rawParameterScenarios

ApiTestDataProvider
│
├── KPI Test Data (from api-test-data.json)
│   └── kpiTestDataSets array
│
├── Raw Parameter Test Data (from api-test-data.json)
│   └── rawParameterTestDataSets array
│
└── Aggregate Test Data (from api-test-data.json)
    └── aggregateTestDataSets array
```

---

## Running Tests: Command Reference

### Run Single Test Class
```bash
mvn test -Dtest=GetKpiLclUclValue
```

### Run Test Against Specific Environment
```bash
mvn test -Dtest=GetKpiLclUclValue -Denvironment=uat
```

### Run All API Tests
```bash
mvn test -Dgroups=API
```

### Run with Verbose Output
```bash
mvn test -Dtest=GetKpiLclUclValue -X
```

### Run with Specific TestNG XML Suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Parallel Execution
```bash
mvn test -Dtest=GetKpiLclUclValue -DforkCount=3 -DreuseForks=true
```

---

## Metrics & Monitoring

### Benefits to Track

| Metric | Before | After | Target |
|--------|--------|-------|--------|
| Time to add test scenario | 15-20 min | 2-3 min | <5 min |
| Test maintenance effort | High | Low | Very Low |
| Configuration changes/month | 10+ | <5 | <3 |
| Hardcoded values in tests | 100+ | 0 | 0 |
| Code duplication for environments | High | None | None |
| Test data reusability | Low | High | Very High |
| Ability to run ad-hoc tests | Limited | Easy | Seamless |

---

## Checklist: Implementation Progress

### Phase 1: Foundation (COMPLETED)
- [x] Create test-config.yml
- [x] Create api-test-data.json
- [x] Implement TestDataConfig
- [x] Implement ApiTestDataProvider
- [x] Implement DataGeneratorUtil
- [x] Create EnterpriseApiBase
- [x] Write comprehensive documentation

### Phase 2: API Test Migration (TODO)
- [ ] Refactor GetKpiLclUclValue
- [ ] Refactor GetRawParamLclUclValue
- [ ] Refactor GetKpiData
- [ ] Refactor GetRawParameterData
- [ ] Refactor Aggregate tests
- [ ] Run full regression test suite
- [ ] Document custom datasets

### Phase 3: UI Test Migration (TODO)
- [ ] Create ui-test-data.json
- [ ] Create UiTestDataProvider
- [ ] Refactor DashboardTest
- [ ] Refactor AdminFlowTest
- [ ] Refactor CreateUserAndDeviceTest
- [ ] Refactor EquipmentDataSetup

### Phase 4: Advanced Features (TODO - OPTIONAL)
- [ ] Implement database provider
- [ ] Set up external configuration server
- [ ] Create environment-specific profiles
- [ ] Implement test data versioning
- [ ] Create centralized test data API

### Phase 5: Deployment (TODO)
- [ ] Update CI/CD pipeline
- [ ] Set up environment-specific Jenkins jobs
- [ ] Create Allure reports integration
- [ ] Document team wiki/Confluence
- [ ] Train team on new approach

---

## Quick Start: Adding New Test Data

### Scenario: You need to add a new KPI test case

**Step 1:** Add entry to `api-test-data.json`
```json
{
  "id": "kpi_new_scenario",
  "testName": "New KPI Test Scenario",
  "definitionId": 10,
  "equipmentId": 4250,
  "machineId": 4250,
  "kpiID": 10,
  "startDateTime": "2026-02-01T08:00:00",
  "endDateTime": "2026-02-02T08:00:00",
  "granularity": 60000
}
```

**Step 2:** Run test
```bash
mvn test -Dtest=GetKpiLclUclValue
```

**That's it!** The test automatically picks up the new dataset and runs with it.

---

## Troubleshooting

### Issue: "Configuration file not found"
**Solution:** Ensure file exists at `src/test/resources/testData/api/test-config.yml`

### Issue: "Test data file not found"
**Solution:** Ensure file exists at `src/test/resources/testData/api/api-test-data.json`

### Issue: DataProvider not providing data
**Solution:** Verify JSON structure matches expected format:
```json
{
  "kpiTestDataSets": [ { "id": "...", ... } ]
}
```

### Issue: DateTime parsing error
**Solution:** Ensure DateTime format is ISO 8601:
```
Correct: 2026-01-26T10:00:00
Incorrect: 2026-01-26 10:00:00
```

---

## Resource Links

- [API Test Data Configuration](src/test/resources/testData/api/api-test-data.json)
- [Environment Configuration](src/test/resources/testData/api/test-config.yml)
- [TestDataConfig Implementation](src/main/java/config/TestDataConfig.java)
- [ApiTestDataProvider Implementation](src/main/java/config/ApiTestDataProvider.java)
- [DataGeneratorUtil Implementation](src/main/java/config/DataGeneratorUtil.java)
- [EnterpriseApiBase Implementation](src/main/java/base/api/EnterpriseApiBase.java)
- [Refactored KPI Test Example](src/test/java/test/api/GetKpiLclUclValueRefactored.java)
- [Refactored Raw Param Test Example](src/test/java/test/api/GetRawParamLclUclValueRefactored.java)

---

## Questions & Support

**Q: Do I need to update all tests at once?**
A: No, migrate incrementally. Start with API tests, then UI tests.

**Q: Can I keep some tests hardcoded?**
A: Yes, but migration is recommended for consistency.

**Q: How do I run tests against production?**
A: `mvn test -Denvironment=prod`

**Q: Can I add custom test data providers?**
A: Yes, extend `ApiTestDataProvider` for your custom logic.

**Q: How do I handle environment-specific test data?**
A: Create environment-specific JSON files and implement loading logic.

---

## Next Steps

1. **Review** the refactored test examples
2. **Pick one test class** to migrate as a pilot
3. **Update test** using the Migration Template
4. **Run tests** to verify they pass
5. **Document results** and team learnings
6. **Scale to other tests** following the same pattern
7. **Celebrate** your enterprise-ready framework! 🎉
