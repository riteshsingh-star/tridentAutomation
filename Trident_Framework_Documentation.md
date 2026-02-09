# Trident Automation Framework Documentation

## Table of Contents
1. [Overview](#overview)
2. [Quick Start](#quick-start)
3. [Architecture](#architecture)
4. [Setup Guide](#setup-guide)
5. [API Testing](#api-testing)
6. [Best Practices](#best-practices)
7. [Troubleshooting](#troubleshooting)

---

## Overview

The Trident Automation Framework is a comprehensive test automation solution built with Java, Playwright, and TestNG for web and API testing. It provides enterprise-grade features including parallel execution, advanced reporting, and multi-environment support.

### Key Features
- **Multi-Browser Support**: Chrome, Firefox, and WebKit testing
- **Parallel Execution**: Thread-safe implementation for parallel test runs
- **API Testing**: Comprehensive REST API testing with Playwright's API capabilities
- **Advanced Reporting**: Allure reports and ExtentReports integration
- **Configuration Management**: Environment-based configuration system
- **Page Object Model**: Clean, maintainable page object design
- **Data-Driven Testing**: JSON-based test data management

### Technology Stack
- **Java 17**: Core programming language
- **Microsoft Playwright 1.43.0**: Web automation engine
- **TestNG 7.11.0**: Test framework and runner
- **Maven**: Dependency management and build tool
- **Allure 2.32.0**: Advanced test reporting
- **Jackson 2.17.0**: JSON processing
- **Log4j2 2.25.3**: Logging framework

### Project Structure
```
tridentAutomation/
├── src/
│   ├── main/java/
│   │   ├── pojo/api/          # API data models
│   │   ├── pojo/web/           # Web page data models
│   │   └── utils/             # Utility classes
│   └── test/
│       ├── java/
│       │   ├── base/          # Base classes
│       │   ├── factory/       # Playwright factory
│       │   ├── listeners/     # TestNG listeners
│       │   ├── page/web/      # Page objects
│       │   └── test/          # Test classes
│       └── resources/
│           ├── config.properties
│           ├── log4j2.xml
│           └── testData/
├── pom.xml
├── testng.xml
└── README.md
```

---

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6.0 or higher
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd tridentAutomation
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure the framework**
   - Edit `src/test/resources/config.properties` with your environment details
   - Update browser settings, URLs, and credentials

4. **Run tests**
   ```bash
   # Run all tests
   mvn test
   
   # Run with specific profile (SIT/UAT)
   mvn test -P sit
   mvn test -P uat
   ```

### Configuration

#### Environment Configuration
```properties
# Browser settings
browser=chrome
headless=false

# SIT Environment
sit.webUrl=https://sit-process.infinite-uptime.com/dashboard
sit.APIBaseURL=https://sit-ipf.infinite-uptime.com
sit.userName=your_username@domain.com
sit.webPassword=your_password
sit.X-ORG-ID=901

# UAT Environment
uat.webUrl=https://uat-new-process.infinite-uptime.com/dashboard
uat.APIBaseURL=https://uat-new-ipf.infinite-uptime.com
uat.X-ORG-ID=1138
```

---

## Architecture

### Design Patterns

#### 1. Factory Pattern
**PlaywrightFactory** manages browser creation and lifecycle:
```java
public class PlaywrightFactory {
    protected static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    protected static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    protected static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    protected static ThreadLocal<Page> tlPage = new ThreadLocal<>();
    
    public static void initBrowser(String browserName, boolean headless) {
        Playwright pw = Playwright.create();
        tlPlaywright.set(pw);
        BrowserType type = switch (browserName.toLowerCase()) {
            case "firefox" -> pw.firefox();
            case "webkit" -> pw.webkit();
            default -> pw.chromium();
        };
        tlBrowser.set(type.launch(new BrowserType.LaunchOptions().setHeadless(headless)));
    }
}
```

#### 2. Page Object Model
**BasePage** provides common element interaction methods:
```java
public class BasePage {
    public Page page;
    public BrowserContext context;
    
    public Locator getByRoleButton(String text, Page page) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(text));
    }
    
    public static void waitAndClick(Page page, Locator locator, int waitMs) {
        page.waitForTimeout(waitMs);
        locator.scrollIntoViewIfNeeded();
        locator.click();
    }
}
```

#### 3. Template Method Pattern
**BaseTest** defines test execution template:
```java
public class BaseTest {
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        String browserName = ReadPropertiesFile.get("browser");
        boolean headless = Boolean.parseBoolean(ReadPropertiesFile.get("headless"));
        PlaywrightFactory.initBrowser(browserName, headless);
        PlaywrightFactory.createContextAndPage();
        page = PlaywrightFactory.getPage();
        context = PlaywrightFactory.getContext();
        page.navigate(baseUrl);
        performLogin();
    }
}
```

### Thread Safety
The framework uses ThreadLocal to ensure thread safety:
- Each thread gets its own browser instance
- Separate contexts prevent cross-test interference
- Automatic cleanup after test completion

---

## Setup Guide

### System Requirements

#### Windows
- Windows 10/11
- JDK 17+
- Maven 3.6.0+
- Git

#### macOS
- macOS 10.15+
- JDK 17+
- Maven 3.6.0+
- Git

#### Linux
- Ubuntu 18.04+
- JDK 17+
- Maven 3.6.0+
- Git

### Installation Steps

#### 1. Java Development Kit
```bash
# Windows: Download from https://adoptium.net/temurin/releases/?version=17
# macOS: brew install openjdk@17
# Linux: sudo apt install openjdk-17-jdk

# Verify installation
java -version
```

#### 2. Apache Maven
```bash
# Windows: Download from https://maven.apache.org/download.cgi
# macOS: brew install maven
# Linux: sudo apt install maven

# Verify installation
mvn -version
```

#### 3. Framework Setup
```bash
git clone <repository-url>
cd tridentAutomation
mvn clean install
```

### IDE Configuration

#### IntelliJ IDEA
1. File → Open → Select `pom.xml`
2. Configure Project SDK to JDK 17
3. Configure Maven settings

#### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Select project directory
3. Configure JDK in preferences

### Running Tests

#### Command Line
```bash
# Run all tests
mvn test

# Run with profile
mvn test -P sit

# Run specific test class
mvn test -Dtest=AdminFlowTest
```

#### IDE
- Right-click on test class → Run As → TestNG Test
- Use Run configurations for custom execution

---

## API Testing

### API Base Infrastructure

The framework's API testing is built around the `APIBase` class:

```java
public class APIBase {
    protected static Playwright playwright;
    protected static APIRequestContext request;
    
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
}
```

### Available Endpoints

#### 1. KPI Time Series Data
**Endpoint**: `/query/api/kpis/timeseries`
**Method**: POST

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

#### 2. Equipment Data
**Endpoint**: `/web/api/kpi-implementation/equipment/{equipmentId}`
**Method**: GET

```java
@Test
public void getEquipmentIDAndData() {
    String equipmentID = ReadPropertiesFile.get("equipmentID");
    APIResponse response = request.get("/web/api/kpi-implementation/equipment/" + equipmentID);
    Assert.assertEquals(response.status(), 200);
    System.out.println(response.text());
}
```

### Data Models

#### Equipment KPI Request
```java
public record EquipKpiRequest(
    List<EquipKpi> equipKpis,
    DateRange dateRange,
    int granularityInMillis,
    boolean addMissingTimestamps
) {}
```

#### Date Range
```java
public record DateRange(
    String startTs,
    String endTs
) {}
```

### Response Handling

#### fetchApiData Method
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
            apiValues.put(convertedTimeStamp, value);
        }
    }
    return apiValues;
}
```

---

## Best Practices

### Test Organization
- Group related tests in logical packages
- Use descriptive test method names
- Implement proper setup and teardown
- Follow Page Object Model principles

### Code Examples

#### Web Test Example
```java
public class SampleTest extends BaseTest {
    
    @Test
    public void sampleWebTest() {
        Dashboard dashboard = new Dashboard(page, context);
        dashboard.navigateToDashboard();
        dashboard.verifyDashboardElements();
    }
}
```

#### API Test Example
```java
public class SampleApiTest extends APIBase {
    
    @Test
    public void sampleApiTest() throws IOException {
        APIResponse response = readJsonFileForApiRequest("sampleRequest", "/api/endpoint");
        assertEquals(response.status(), 200);
        
        String responseBody = response.text();
        Map<String, String> apiData = fetchApiData(responseBody, "data", 0, "values", 0, "items", false);
    }
}
```

### Security
- Never commit credentials to version control
- Use environment variables for sensitive data
- Implement proper authentication handling
- Validate all inputs and outputs

### Performance
- Optimize test execution time
- Use parallel execution where possible
- Implement efficient wait strategies
- Manage resources properly

---

## Troubleshooting

### Common Issues

#### 1. Java Version Issues
**Problem**: `UnsupportedClassVersionError`
**Solution**: Ensure you're using Java 17 or higher:
```bash
java -version
javac -version
```

#### 2. Browser Launch Issues
**Problem**: `Browser failed to launch`
**Solution**: 
- Check browser installation
- Verify browser driver compatibility
- Try running in headless mode

#### 3. Authentication Issues
**Problem**: `401 Unauthorized` API responses
**Solution**:
- Verify auth token in `config.properties`
- Check token expiration
- Validate organization ID

#### 4. Port Conflicts
**Problem**: `Port already in use`
**Solution**:
- Identify and kill processes using the port
- Configure different ports in test configuration

### Debug Mode

Enable debug logging:
```xml
<!-- log4j2.xml -->
<Root level="debug">
    <AppenderRef ref="Console"/>
    <AppenderRef ref="File"/>
</Root>
```

### Reporting

#### Allure Reports
```bash
# Generate and serve report
allure serve target/allure-results

# Generate static report
allure generate target/allure-results -o target/allure-report
```

#### ExtentReports
Automatically generated and can be found in the `reports/` directory.

---

## Continuous Integration

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    tools {
        maven 'Maven-3.8'
        jdk 'JDK-17'
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/your-org/tridentAutomation.git'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn clean test -P uat'
            }
        }
        stage('Reports') {
            steps {
                allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
            }
        }
    }
}
```

### GitHub Actions
```yaml
name: Trident Framework Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Run tests
      run: mvn clean test -P uat
    - name: Generate Allure Report
      uses: simple-elf/allure-report-action@master
      if: always()
      with:
        allure-results: target/allure-results
```

---

## Conclusion

The Trident Automation Framework provides a robust, scalable solution for web and API testing. With its modular architecture, comprehensive reporting, and multi-environment support, it enables teams to implement efficient test automation strategies.

### Key Benefits
- **Scalability**: Parallel execution and thread-safe design
- **Maintainability**: Clean architecture and design patterns
- **Flexibility**: Multi-browser and multi-environment support
- **Reliability**: Comprehensive error handling and reporting
- **Extensibility**: Plugin-based architecture for custom enhancements

### Next Steps
1. Set up your development environment
2. Configure your test environments
3. Create your first test cases
4. Integrate with your CI/CD pipeline
5. Monitor and optimize test execution

---

**Version**: 1.0-SNAPSHOT  
**Last Updated**: February 2025  
**Framework**: Trident Automation Framework  
**Technology Stack**: Java 17, Playwright, TestNG, Maven

For additional support or questions, refer to the project repository or contact the framework development team.
