# Trident Automation Framework

A comprehensive test automation framework built with Java, Playwright, and TestNG for web and API testing.

## Overview

The Trident Automation Framework is a robust, scalable testing solution designed for end-to-end automation of web applications and REST APIs. It leverages Microsoft Playwright for web automation, TestNG for test management, and provides extensive reporting capabilities with Allure and ExtentReports.

## Features

- **Multi-Browser Support**: Chrome, Firefox, and WebKit testing
- **Parallel Execution**: Thread-safe implementation for parallel test runs
- **API Testing**: Comprehensive REST API testing with Playwright's API capabilities
- **Advanced Reporting**: Allure reports and ExtentReports integration
- **Configuration Management**: Environment-based configuration system
- **Page Object Model**: Clean, maintainable page object design
- **Data-Driven Testing**: JSON-based test data management
- **Logging**: Log4j2 integration for detailed logging
- **Screenshot Capture**: Automatic screenshots on test failures

## Technology Stack

- **Java 17**: Core programming language
- **Microsoft Playwright 1.43.0**: Web automation engine
- **TestNG 7.11.0**: Test framework and runner
- **Maven**: Dependency management and build tool
- **Allure 2.32.0**: Advanced test reporting
- **ExtentReports 5.1.2**: HTML reporting
- **Jackson 2.17.0**: JSON processing
- **Lombok 1.18.30**: Code generation
- **Log4j2 2.25.3**: Logging framework

## Project Structure

```
tridentAutomation/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── pojo/
│   │       │   ├── api/          # API data models
│   │       │   └── web/           # Web page data models
│   │       └── utils/             # Utility classes
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   │   ├── api/           # API test base class
│       │   │   └── web/           # Web test base classes
│       │   ├── factory/           # Playwright factory
│       │   ├── listeners/         # TestNG listeners
│       │   ├── page/
│       │   │   └── web/           # Page objects
│       │   └── test/
│       │       ├── api/           # API test classes
│       │       └── web/           # Web test classes
│       └── resources/
│           ├── config.properties   # Configuration file
│           ├── log4j2.xml         # Logging configuration
│           ├── testData/          # Test data files
│           └── APIRequests/       # API request payloads
├── pom.xml                        # Maven configuration
├── testng.xml                     # TestNG configuration
└── README.md                      # This file
```

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
   
   # Run specific test class
   mvn test -Dtest=AdminFlowTest
   ```

## Configuration

### Environment Configuration

The framework supports multiple environments through Maven profiles:

- **SIT**: System Integration Testing
- **UAT**: User Acceptance Testing

Configuration is managed through `config.properties`:

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

### Test Data Management

Test data is stored in JSON format under `src/test/resources/testData/`. The framework includes utilities for reading and parsing JSON test data.

## Core Components

### Base Classes

#### BaseTest (Web)
- Initializes Playwright browser and context
- Handles login functionality
- Manages test lifecycle (setup/teardown)
- Provides common web testing utilities

#### APIBase
- Initializes API request context
- Handles authentication and headers
- Provides common API testing methods
- Manages API response parsing

### Factory Pattern

#### PlaywrightFactory
- Thread-safe browser management
- Supports multiple browsers (Chrome, Firefox, WebKit)
- Context and page creation
- Resource cleanup

### Page Object Model

All web pages extend `BasePage` which provides:
- Common element locator methods
- Wait utilities
- Click and fill actions
- Element interaction helpers

### Utilities

- **WaitUtils**: Custom wait strategies
- **ReadPropertiesFile**: Configuration management
- **ReadJsonFile**: JSON data parsing
- **ExtentManager**: Report generation
- **ParseTheTimeFormat**: Date/time utilities
- **CompareGraphAndApiData**: Data validation

## Test Execution

### Running Tests

1. **Via Maven**
   ```bash
   mvn test
   ```

2. **Via IDE**
   - Run `testng.xml` configuration
   - Right-click on test classes and run

3. **Parallel Execution**
   - Tests run in parallel by default (thread-count="2")
   - Configure thread count in `testng.xml`

### Test Reports

After test execution, reports are generated in:

- **Allure Reports**: `target/allure-results/`
  ```bash
  allure serve target/allure-results
  ```

- **ExtentReports**: Generated programmatically
- **Screenshots**: `screenshots/` directory (on failures)

## Writing Tests

### Web Tests

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

### API Tests

```java
public class SampleApiTest extends APIBase {
    
    @Test
    public void sampleApiTest() throws IOException {
        APIResponse response = readJsonFileForApiRequest("sampleRequest", "/api/endpoint");
        assertEquals(response.status(), 200);
        
        // Parse response
        String responseBody = response.text();
        Map<String, String> apiData = fetchApiData(responseBody, "data", 0, "values", 0, "items", false);
    }
}
```

## Best Practices

1. **Page Objects**: Keep page logic separate from test logic
2. **Data-Driven**: Use external data files for test data
3. **Assertions**: Use TestNG assertions for validations
4. **Logging**: Add meaningful logs for debugging
5. **Error Handling**: Implement proper try-catch blocks
6. **Clean Code**: Follow Java coding standards

## Troubleshooting

### Common Issues

1. **Browser Launch Failures**
   - Check browser installation
   - Verify headless mode settings

2. **Authentication Failures**
   - Validate auth tokens in config
   - Check API endpoints and headers

3. **Element Not Found**
   - Verify locators and waits
   - Check page load timing

4. **Parallel Execution Issues**
   - Ensure thread safety
   - Check shared resource usage

### Debug Mode

Enable debug logging in `log4j2.xml`:
```xml
<Root level="debug">
    <AppenderRef ref="Console"/>
</Root>
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Contact the framework development team
- Check the documentation and existing issues

---

**Version**: 1.0-SNAPSHOT  
**Last Updated**: 2025-02-09
