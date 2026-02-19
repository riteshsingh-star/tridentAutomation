# Trident Automation Framework

A comprehensive test automation framework built with Playwright, Java, and TestNG for web and API testing.

## Overview

The Trident Automation Framework is a robust, scalable, and maintainable test automation solution designed for testing
web applications and REST APIs. It leverages Microsoft Playwright for web automation, TestNG for test execution, and
includes comprehensive reporting capabilities with Allure and ExtentReports.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Writing Tests](#writing-tests)
- [Page Object Model](#page-object-model)
- [API Testing](#api-testing)
- [Environment Management](#environment-management)
- [Logging](#logging)
- [Troubleshooting](#troubleshooting)

## Features

- **Multi-Browser Support**: Chrome, Firefox, WebKit
- **Parallel Test Execution**: Configurable thread count for faster test runs
- **Page Object Model**: Clean and maintainable test architecture
- **API Testing**: RESTful API testing with comprehensive validation
- **Environment Management**: Support for SIT, UAT, and multiple tenants
- **Comprehensive Reporting**: Allure and ExtentReports integration
- **Logging**: Log4j2-based detailed logging
- **Screenshot Capture**: Automatic screenshots on test failures
- **Cross-Platform**: Works on Windows, macOS, and Linux

## Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.6.0 or higher
- **Node.js**: Required for Playwright browser installation
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code

## Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd folderName
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Install Playwright browsers**
   ```bash
   mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install"
   ```

## Project Structure

```
tridentAutomation/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── config/              # Configuration management
│   │       │   ├── ApiTestDataProvider.java  # API test data provider
│   │       │   ├── ConfigKey.java            # Configuration keys enum
│   │       │   ├── DataGeneratorUtil.java    # Data generation utilities
│   │       │   ├── EnvironmentConfig.java     # Environment configuration
│   │       │   ├── ReadPropertiesFile.java    # Properties file reader
│   │       │   └── TestDataConfig.java       # Test data configuration
│   │       ├── pojo/                # Plain Old Java Objects
│   │       │   ├── api/              # API POJO classes
│   │       │   │   ├── DateRange.java            # Date range for API requests
│   │       │   │   ├── EquipKpi.java             # Equipment KPI data model
│   │       │   │   ├── EquipKpiRequest.java      # Equipment KPI request model
│   │       │   │   ├── KpiAggregateRequest.java  # KPI aggregate request model
│   │       │   │   ├── RawRequest.java           # Raw parameter request model
│   │       │   │   └── Raws.java                 # Raw parameter data model
│   │       │   └── web/              # Web POJO classes
│   │       │       ├── AdminFlow.java            # Admin flow data model
│   │       │       ├── BaseData.java             # Base data model
│   │       │       ├── DashboardData.java        # Dashboard data model
│   │       │       ├── EquipmentMeasureValidation.java # Equipment validation data
│   │       │       └── UserAndDevice.java        # User and device data model
│   │       └── utils/              # Utility classes
│   │           ├── ApiHelper.java              # API response processing utilities
│   │           ├── CalenderUtil.java            # Calendar and date utilities
│   │           ├── CompareGraphAndApiData.java  # Graph vs API data comparison
│   │           ├── ExtentManager.java           # ExtentReports management
│   │           ├── KPISCalculationUtils.java    # KPI calculation utilities
│   │           ├── LclUclResult.java             # LCL/UCL result data model
│   │           ├── LclUclUtil.java               # LCL/UCL calculation utilities
│   │           ├── ParseTheTimeFormat.java       # Time format parsing utilities
│   │           ├── ReadJsonFile.java            # JSON file reading utilities
│   │           ├── StatisticsComparison.java     # Statistics comparison utilities
│   │           ├── Stats.java                    # Statistical calculations
│   │           └── WaitUtils.java                # Wait and synchronization utilities
│   └── test/
│       ├── java/
│       │   ├── base/                 # Base classes
│       │   │   ├── api/              # API base class
│       │   │   │   └── APIBase.java               # Base class for API tests
│       │   │   └── web/              # Web base class
│       │   │       ├── BasePage.java             # Base page class for POM
│       │   │       └── BaseTest.java             # Base test class for web tests
│       │   ├── factory/              # Playwright factory
│       │   │   ├── ApiFactory.java             # API request context factory
│       │   │   └── WebFactory.java             # Browser and page management
│       │   ├── listeners/            # TestNG listeners
│       │   │   └── Listeners.java                # Test execution listeners
│       │   ├── page/                 # Page objects
│       │   │   ├── api/              # API page objects
│       │   │   │   ├── GetKpiData.java              # KPI data API page
│       │   │   │   ├── GetKpiRequest.java          # KPI request API page
│       │   │   │   ├── GetRawParamRequest.java     # Raw parameter request API page
│       │   │   │   ├── GetRawParameterData.java    # Raw parameter data API page
│       │   │   │   └── GetStatisticsDataFromAPI.java # Statistics data API page
│       │   │   └── web/              # Web page objects
│       │   │       ├── CreateAreas.java          # Area creation page
│       │   │       ├── CreateDevice.java         # Device creation page
│       │   │       ├── CreateEquipment.java      # Equipment creation page
│       │   │       ├── CreateGlobalParameter.java # Global parameter creation
│       │   │       ├── CreateNewKPIDefinition.java # KPI definition creation
│       │   │       ├── CreateOrganization.java    # Organization creation page
│       │   │       ├── CreatePlant.java           # Plant creation page
│       │   │       ├── CreateUsers.java           # User creation page
│       │   │       ├── CreateWidget.java          # Widget creation page
│       │   │       ├── Dashboard.java             # Dashboard page
│       │   │       ├── DashboardVerify.java       # Dashboard verification
│       │   │       ├── EquipmentPageDataVerification.java # Equipment data verification
│       │   │       ├── KPIAdvanceImplementation.java # Advanced KPI implementation
│       │   │       ├── LoginPage.java             # Login page
│       │   │       └── PageComponent.java         # Common page components
│       │   └── test/                 # Test classes
│       │       ├── api/              # API tests
│       │       │   ├── ApiAggregateVerification.java # API aggregate verification
│       │       │   ├── GetAggregateValueAndTypeOfKpi.java # Get aggregate KPI values
│       │       │   ├── GetEquipment.java          # Equipment API test
│       │       │   ├── GetKpiLclUclValue.java     # KPI LCL/UCL value test
│       │       │   ├── GetKpiLclUclValueRefactored.java # Refactored KPI LCL/UCL test
│       │       │   ├── GetRawParamLclUclValue.java # Raw parameter LCL/UCL test
│       │       │   └── GetRawParamLclUclValueRefactored.java # Refactored raw parameter test
│       │       └── web/              # Web tests
│       │           ├── AdminFlowTest.java         # Admin flow test
│       │           ├── AdminGraphValidationTest.java # Admin graph validation
│       │           ├── CreateUserAndDeviceTest.java # User and device creation test
│       │           ├── DashboardTest.java         # Dashboard functionality test
│       │           ├── DashboardVerify.java       # Dashboard verification test
│       │           ├── EquipmentDataSetup.java    # Equipment data setup test
│       │           └── GraphVsApiValidationTest.java # Graph vs API validation
│       └── resources/
│           ├── config.properties     # Configuration file
│           ├── log4j2.xml           # Log4j2 configuration
│           ├── testng.xml          # TestNG suite
│           └── testData/           # Test data files
│               ├── adminFlow.json              # Admin flow test data
│               ├── api/                       # API test data
│               │   ├── kpiAggregate.json       # KPI aggregate data
│               │   └── rawParameterAggregate.json # Raw parameter aggregate data
│               ├── baseData.json              # Base test data
│               ├── clientSpecific/             # Client-specific data
│               │   ├── hindalco.json          # Hindalco client data
│               │   └── mrf.json                # MRF client data
│               ├── dashboardData.json          # Dashboard test data
│               ├── equipmentMeasureValidation.json # Equipment validation data
│               ├── rawParameter.json           # Raw parameter data
│               └── userAndDevice.json           # User and device data
├── .github/workflows/                # CI/CD workflows
│   └── playwrightJava.yml            # GitHub Actions workflow
├── target/allure-results/            # Allure test results
├── target/extent-reports/            # Extent report support
├── logs/                            # Application logs
│   └── automation.log               # Main automation log file
├── screenshots/                     # Test failure screenshots
├── pom.xml                          # Maven configuration
├── testng.xml                       # TestNG configuration
├── README.md                        # Project documentation
└── Trident_Automation_Framework_Documentation.txt # Framework documentation
```

## Configuration

### Environment Configuration

The framework uses `config.properties` for configuration. Key properties include:

```properties
# Browser Configuration
browser=chrome
headless=false
# Common URLs
timeSeriesPathURL=/query/api/kpis/timeseries
rawParameterPathURL=/query/api/raw-parameters/timeseries
aggregatePathURL=/query/api/kpis/aggregates
# SIT Environment
sit.webUrl=https://sit-process.infinite-uptime.com/dashboard
sit.APIBaseURL=https://sit-ipf.infinite-uptime.com
sit.userName=covacsis_admin@techprescient.com
sit.webPassword=your_password
sit.X-ORG-ID=901
# UAT Environment
uat.webUrl=https://uat-new-process.infinite-uptime.com/dashboard
uat.APIBaseURL=https://uat-new-ipf.infinite-uptime.com
uat.webPassword=your_password
uat.X-ORG-ID=1138
```

### Maven Profiles

Use Maven profiles to switch between environments:

```bash
# SIT Environment
mvn test -P sit

# UAT Environment
mvn test -P uat
```

## Running Tests

### Basic Test Execution

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AdminFlowTest

```

### Parallel Execution

Tests are configured to run in parallel with configurable thread count in `testng.xml`:

```xml

<suite name="PlaywrightJava" parallel="tests" thread-count="2">
```

### Running with Allure

```bash
# Run tests and generate Allure report
mvn clean test allure:report

# Serve Allure report
mvn allure:serve
```

## Test Reports

### Allure Reports

- **Location**: `target/allure-results/`
- **View**: Run `mvn allure:serve` to view live reports
- **Features**: Interactive charts, test history, attachments

### ExtentReports

- **Location**: `target/extent-reports/`
- **Features**: Rich HTML reports with screenshots and logs

### TestNG Reports

- **Location**: `target/surefire-reports/`
- **Format**: XML and HTML reports

## Writing Tests

### Web Tests

Extend `BaseTest` for web automation:

```java
public class SampleTest extends BaseTest {

    @Test
    public void testSampleFunctionality() {
        Dashboard dashboard = new Dashboard(page, context);
        dashboard.navigateToDashboard();
        dashboard.verifyDashboardElements();
    }
}
```

### API Tests

Extend `APIBase` for API testing:

```java
public class SampleApiTest extends APIBase {

    @Test
    public void testApiEndpoint() {
        Response response = getApiRequest("/api/endpoint");
        assertEquals(response.statusCode(), 200);
    }
}
```

## Page Object Model

### Creating Page Objects

```java
public class LoginPage extends BasePage {

    Locator userName;
    Locator password;
    Locator loginButton;

    public LoginPage(Page page, BrowserContext browserContext) {
        super(page, browserContext);
        this.userName = getByRoleTextbox(page);
        this.password = getByRoleTextbox(page);
        this.loginButton = getByRoleLabelText(page, "Login");
    }

    private static final Logger log = LogManager.getLogger(LoginPage.class);

    public void login(String user, String pass) {
        enterUserName(userName, user);
        enterPassword(password, pass);
        clickLoginButton();
    }

}
```

### BasePage Features

- **Playwright Locators**: Role-based element identification (button, textbox, link, etc.)
- **Common Actions**: Reusable methods for click, fill, wait with scroll-into-view
- **Multiple Locator Strategies**: getByRole, getByText, getByPlaceholder, getByLabel
- **Wait Strategies**: Built-in synchronization with WaitUtils integration
- **Allure Integration**: Step-by-step test reporting
- **Logging**: Comprehensive Log4j2 integration
- **Error Handling**: Automatic retry and detailed error logging
- **Screenshots**: Automatic capture on failures

## API Testing

### API Base Class

The `APIBase` class provides:

- **HTTP Methods**: GET, POST, PUT, DELETE
- **Request Configuration**: Headers, authentication, parameters
- **Response Validation**: Status codes, response body, headers
- **JSON Handling**: Jackson-based JSON parsing

### Example API Test

```java
public class GetKpiData extends APIBase {

    @Test
    public void testGetKpiData() {
        String endpoint = "/query/api/kpis/timeseries";
        Response response = getApiRequest(endpoint);

        assertEquals(response.statusCode(), 200);

        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.get("data"));
    }
}
```

## Environment Management

### Supported Environments

- **SIT**: System Integration Testing
- **UAT**: User Acceptance Testing
- **Multiple Tenants**: Dev, Hindalco, MRF, Arvind Mills, MTR, Textile, IU

### Tenant Configuration

Each tenant has dedicated credentials and configurations:

```properties
# Dev Tenant
uat.dev.user.userName=dev_user@techprescient.com
uat.dev.admin.userName=dev_admin@techprescient.com
# Hindalco Tenant
uat.hindalco_mouda.user.userName=hindalco_mouda_user@techprescient.com
uat.hindalco_mouda.admin.userName=hindalco_mouda_admin@techprescient.com
```

## Logging

### Log4j2 Configuration

- **Configuration**: `src/test/resources/log4j2.xml`
- **Log Levels**: DEBUG, INFO, WARN, ERROR
- **Appenders**: Console and file appenders
- **Location**: `logs/automation.log`

### Log Messages

```java
private static final Logger log = LogManager.getLogger(YourClass.class);

log.

info("Test execution started");
log.

error("Test failed: {}",errorMessage);
log.

debug("Element found: {}",element);
```

## Troubleshooting

### Common Issues

1. **Browser Launch Failure**
    - Ensure Playwright browsers are installed
    - Check browser compatibility with your OS

2. **Authentication Issues**
    - Verify auth token validity
    - Check environment-specific credentials

3. **Element Not Found**
    - Increase wait times
    - Verify element locators
    - Check iframes and shadow DOM

4. **Test Failures**
    - Check logs in `logs/automation.log`
    - Review screenshots in `screenshots/` folder
    - Analyze Allure reports for detailed failure information

### Debug Mode

Enable debug mode by setting:

```properties
# In config.properties
headless=false
# Add VM option: -Dlog4j.configurationFile=path/to/log4j2-debug.xml
```

