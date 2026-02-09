# Trident Automation Framework Documentation

## Table of Contents
1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Technology Stack](#technology-stack)
4. [Project Structure](#project-structure)
5. [Setup and Configuration](#setup-and-configuration)
6. [Core Components](#core-components)
7. [Test Execution](#test-execution)
8. [Reporting](#reporting)
9. [Best Practices](#best-practices)
10. [Troubleshooting](#troubleshooting)

---

## Overview

The Trident Automation Framework is a comprehensive test automation solution built using Java, Playwright, and TestNG. It is designed for end-to-end testing of web applications and API testing with advanced reporting capabilities.

### Key Features
- **Multi-Browser Support**: Chrome, Firefox, and WebKit
- **Parallel Test Execution**: Configurable thread count
- **Advanced Reporting**: ExtentReports and Allure integration
- **API Testing**: RESTful API testing capabilities
- **Environment Management**: Support for SIT, UAT environments
- **Multi-Tenant Support**: Configurable for different clients
- **Screenshot Capture**: Automatic screenshots on test failure
- **Logging**: Comprehensive logging with Log4j2

---

## Architecture

### Framework Layers

```
┌─────────────────────────────────────┐
│           Test Layer                │
│        (TestNG Test Classes)        │
├─────────────────────────────────────┤
│          Page Layer                 │
│     (Page Objects & API Classes)    │
├─────────────────────────────────────┤
│         Business Layer              │
│      (Utilities & Helpers)          │
├─────────────────────────────────────┤
│          Base Layer                 │
│    (BaseTest, BasePage, APIBase)   │
├─────────────────────────────────────┤
│         Configuration               │
│    (Properties, Listeners)          │
└─────────────────────────────────────┘
```

---

## Technology Stack

### Core Dependencies
- **Java 17**: Programming language
- **Playwright 1.43.0**: Web automation
- **TestNG 7.11.0**: Test framework
- **Maven**: Build management

### Reporting & Utilities
- **ExtentReports 5.1.2**: HTML reports
- **Allure 2.32.0**: Advanced reporting
- **Log4j2 2.25.3**: Logging framework
- **Jackson 2.17.0**: JSON processing
- **Lombok 1.18.30**: Code generation

---

## Project Structure

```
tridentAutomation/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── pojo/                 # Data Transfer Objects
│   │       │   ├── AdminFlow.java
│   │       │   ├── BaseData.java
│   │       │   ├── DashboardData.java
│   │       │   └── api/              # API POJOs
│   │       └── utils/                # Utility Classes
│   │           ├── CalenderUtil.java
│   │           ├── ExtentManager.java
│   │           ├── ReadPropertiesFile.java
│   │           ├── WaitUtils.java
│   │           └── ...
│   └── test/
│       ├── java/
│       │   ├── base/                 # Base Classes
│       │   │   ├── web/
│       │   │   │   ├── BaseTest.java
│       │   │   │   └── BasePage.java
│       │   │   └── api/
│       │   │       └── APIBase.java
│       │   ├── listeners/            # TestNG Listeners
│       │   │   └── Listeners.java
│       │   ├── page/                 # Page Objects
│       │   │   ├── web/              # Web Page Objects
│       │   │   │   ├── LoginPage.java
│       │   │   │   ├── CreateDevice.java
│       │   │   │   └── ...
│       │   │   └── api/              # API Page Objects
│       │   │       ├── GetKpiData.java
│       │   │       ├── GetEquipment.java
│       │   │       └── ...
│       │   └── test/                 # Test Classes
│       │       └── AdminFlowTest.java
│       └── resources/
│           ├── config.properties     # Configuration
│           ├── log4j2.xml           # Logging Configuration
│           ├── testData/            # Test Data
│           └── APIRequests/         # API Request Templates
├── pom.xml                          # Maven Configuration
├── testng.xml                       # TestNG Configuration
├── allure-results/                  # Allure Report Output
├── reports/                         # ExtentReports Output
├── screenshots/                     # Test Failure Screenshots
└── logs/                           # Application Logs
```

---

## Setup and Configuration

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA recommended)

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd tridentAutomation
   ```

2. **Install Dependencies**
   ```bash
   mvn clean install
   ```

3. **Install Playwright Browsers**
   ```bash
   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
   ```

### Configuration

#### Environment Configuration (`config.properties`)

The framework supports multiple environments and tenants:

```properties
# Common Settings
browser=chrome
headless=false

# Environment URLs
sit.webUrl=https://sit-process.infinite-uptime.com/dashboard
uat.webUrl=https://uat-new-process.infinite-uptime.com/dashboard

# API Configuration
sit.APIBaseURL=https://sit-ipf.infinite-uptime.com
uat.APIBaseURL=https://uat-new-ipf.infinite-uptime.com

# Multi-Tenant Configuration
uat.mtr.admin.userName=mtr_admin@techprescient.com
uat.hindalco_mouda.admin.userName=hindalco_mouda_admin@techprescient.com
```

#### Running Tests with Different Profiles

```bash
# SIT Environment
mvn test -P sit

# UAT Environment
mvn test -P uat

# Specific Tenant
mvn test -P uat -Dclient=mtr -DuserType=admin
```

---

## Core Components

### Base Classes

#### BaseTest.java
The foundation for all web test classes:

```java
public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    
    @BeforeClass
    public void setUp() {
        // Browser initialization
        // Login automation
        // Context setup
    }
}
```

**Key Features:**
- Automatic browser setup based on configuration
- Built-in login functionality
- Support for headless/headed mode
- Multi-browser support (Chrome, Firefox, WebKit)

#### BasePage.java
Common page interactions and utilities:

```java
public class BasePage {
    public Page page;
    
    // Locator strategies
    public Locator getByRoleButton(String text, Page page)
    public Locator getByPlaceholder(String placeholderValue, Page page)
    public Locator getByText(String text, Page page)
    
    // Wait strategies
    public static void waitAndClick(Page page, Locator locator, int waitMs)
    public static void waitAndFill(Page page, Locator locator, String text, int waitMs)
}
```

#### APIBase.java
Foundation for API testing:

```java
public class APIBase {
    protected static final Logger log = LogManager.getLogger(APIBase.class);
    protected Request request;
    protected APIRequestContext apiRequestContext;
    
    // API setup and configuration
    // Common HTTP methods
    // Response handling
}
```

### Page Object Model

#### LoginPage.java
Example of page object implementation:

```java
public class LoginPage extends BasePage {
    Locator userName;
    Locator password;
    Locator loginButton;
    
    public LoginPage(Page page) {
        super(page);
        // Initialize locators
    }
    
    public void login(String user, String pass) {
        enterUserName(userName, user);
        enterPassword(password, pass);
        clickLoginButton();
    }
}
```

### Utility Classes

#### ReadPropertiesFile.java
Dynamic property reading with environment and tenant support:

```java
public class ReadPropertiesFile {
    public static String get(String key) {
        // Priority order:
        // 1. env.client.userType.key
        // 2. env.client.key
        // 3. env.key
        // 4. key
    }
}
```

#### WaitUtils.java
Advanced waiting strategies:

```java
public class WaitUtils {
    public static void waitForVisible(Locator locator, int timeout)
    public static void waitForClickable(Locator locator, int timeout)
    public static void waitForElementToDisappear(Locator locator, int timeout)
}
```

---

## Test Execution

### Running Tests

#### Command Line
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AdminFlowTest

# Run with specific profile
mvn test -P uat

# Parallel execution
mvn test -Dparallel=tests -DthreadCount=4
```

#### TestNG Configuration (`testng.xml`)
```xml
<suite name="PlaywrightJava" parallel="tests" thread-count="2">
    <listeners>
        <listener class-name="listeners.Listeners"/>
    </listeners>
    <test name="PlaywrightTrident">
        <classes>
            <class name="test.AdminFlowTest"/>
        </classes>
    </test>
</suite>
```

### Test Structure Example

```java
public class AdminFlowTest extends BaseTest {
    
    @BeforeClass
    public void setup() {
        // Test-specific setup
        data = ReadJsonFile.readJson("testData/adminFlow.json", AdminFlow.class);
    }
    
    @Test
    public void validateGlobalParameterAndKPITest() {
        // Test implementation
        kpiAdvance.addLogicToTheKPIAndValidate(
            data.globalParameterName(), 
            data.defineKPIName(),
            // ... other parameters
        );
    }
}
```

---

## Reporting

### ExtentReports Integration
- HTML reports with detailed test information
- Screenshots on failure
- Test execution timeline
- Categorized test results

### Allure Reports
- Interactive test reports
- Test history and trends
- Detailed test steps
- Attachments and screenshots

### Report Generation
```bash
# Generate Allure Report
allure serve allure-results

# Generate ExtentReports (automatic)
mvn test
# Reports available in: reports/
```

---

## Best Practices

### Test Development
1. **Page Object Model**: Use page objects for all web interactions
2. **Data-Driven Testing**: Use JSON files for test data
3. **Proper Waits**: Use explicit waits instead of Thread.sleep()
4. **Logging**: Add meaningful logs for debugging
5. **Assertions**: Use descriptive assertion messages

### Code Organization
1. **Package Structure**: Follow the established package hierarchy
2. **Naming Conventions**: Use descriptive names for classes and methods
3. **Comments**: Add JavaDoc for public methods
4. **Error Handling**: Implement proper exception handling

### Configuration Management
1. **Environment Separation**: Use different profiles for environments
2. **Sensitive Data**: Store credentials in properties files
3. **Version Control**: Exclude sensitive files from Git

---

## Troubleshooting

### Common Issues

#### Browser Launch Failures
```bash
# Reinstall Playwright browsers
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

#### Test Failures Due to Waits
- Increase wait timeouts in `WaitUtils.java`
- Check element locators in page objects
- Verify page load completion

#### Configuration Issues
- Verify `config.properties` syntax
- Check environment-specific properties
- Validate Maven profiles

### Debug Mode
Enable debug logging in `log4j2.xml`:
```xml
<Root level="debug">
    <AppenderRef ref="Console"/>
    <AppenderRef ref="File"/>
</Root>
```

### Headless vs Headed Mode
Configure in `config.properties`:
```properties
headless=false  # For debugging
headless=true   # For CI/CD
```

---

## API Testing

### API Base Class Usage
```java
public class GetKpiData extends APIBase {
    
    public Response getKpiData(String kpiId, String dateRange) {
        // API request implementation
        return apiRequestContext.get(url, options);
    }
}
```

### API Test Structure
- Request/Response POJOs in `pojo/api/`
- API page objects in `page/api/`
- Common utilities in `utils/`

---

## Data Management

### Test Data Structure
```json
{
  "globalParameterName": "Temperature",
  "defineKPIName": "Temperature_KPI",
  "plantName": "Plant_1",
  "machineName": "Machine_1",
  "aggregateType": "AVERAGE",
  "KPIPerformanceCriteria": "HIGHER_IS_BETTER"
}
```

### JSON Reading Utility
```java
AdminFlow data = ReadJsonFile.readJson("testData/adminFlow.json", AdminFlow.class);
```

---

## Continuous Integration

### Maven Goals for CI
```bash
# Clean and compile
mvn clean compile

# Run tests with coverage
mvn test jacoco:report

# Generate reports
mvn allure:report
```

### Docker Support (Optional)
```dockerfile
FROM maven:3.9-openjdk-17
# Install Playwright browsers
# Copy project files
# Run tests
```

---

## Conclusion

The Trident Automation Framework provides a robust, scalable solution for web and API testing. With its modular architecture, comprehensive reporting, and multi-environment support, it enables teams to implement efficient test automation strategies.

For additional support or questions, refer to the project documentation or contact the framework development team.

---

**Version**: 1.0  
**Last Updated**: February 2025  
**Framework**: Trident Automation Framework
