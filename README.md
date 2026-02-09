# Trident Automation Framework

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Playwright](https://img.shields.io/badge/Playwright-1.43.0-blue.svg)](https://playwright.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.11.0-green.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)

A comprehensive test automation framework built with Java, Playwright, and TestNG for end-to-end web and API testing with advanced reporting capabilities.

## 🚀 Quick Start

### Prerequisites
- **Java 17** or higher
- **Maven 3.6** or higher
- **Git** for version control

### Installation

```bash
# Clone the repository
git clone <repository-url>
cd tridentAutomation

# Install dependencies
mvn clean install

# Install Playwright browsers
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

# Run tests
mvn test
```

## 📋 Table of Contents

- [Features](#-features)
- [Project Structure](#-project-structure)
- [Configuration](#-configuration)
- [Running Tests](#-running-tests)
- [Reports](#-reports)
- [Environment Setup](#-environment-setup)
- [Contributing](#-contributing)
- [Troubleshooting](#-troubleshooting)

## ✨ Features

- **🌐 Multi-Browser Support**: Chrome, Firefox, and WebKit
- **⚡ Parallel Execution**: Configurable thread count for faster test runs
- **📊 Advanced Reporting**: ExtentReports and Allure integration
- **🔌 API Testing**: RESTful API testing capabilities
- **🏗️ Environment Management**: Support for SIT, UAT environments
- **👥 Multi-Tenant Support**: Configurable for different clients
- **📸 Screenshot Capture**: Automatic screenshots on test failure
- **📝 Comprehensive Logging**: Log4j2 integration
- **🎯 Page Object Model**: Clean and maintainable test structure
- **📄 Data-Driven Testing**: JSON-based test data management

## 📁 Project Structure

```
tridentAutomation/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── pojo/                 # Data Transfer Objects
│   │       │   ├── AdminFlow.java
│   │       │   ├── BaseData.java
│   │       │   └── api/              # API POJOs
│   │       └── utils/                # Utility Classes
│   │           ├── CalenderUtil.java
│   │           ├── ExtentManager.java
│   │           ├── ReadPropertiesFile.java
│   │           └── WaitUtils.java
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
│       │   │       └── GetEquipment.java
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

## ⚙️ Configuration

### Environment Configuration

The framework supports multiple environments and tenants. Configure your settings in `src/test/resources/config.properties`:

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
uat.mrf.admin.userName=mrf_admin@techprescient.com
```

### Browser Configuration

Supported browsers:
- `chrome` (default)
- `firefox`
- `webkit`

### Test Data

Test data is stored in JSON format under `src/test/resources/testData/`:

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

## 🏃 Running Tests

### Command Line Options

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AdminFlowTest

# Run with specific environment profile
mvn test -P uat

# Run with specific tenant
mvn test -P uat -Dclient=mtr -DuserType=admin

# Run tests in parallel
mvn test -Dparallel=tests -DthreadCount=4

# Run tests in headless mode
mvn test -Dheadless=true
```

### TestNG Configuration

Tests are configured in `testng.xml`:

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

### IDE Configuration

For IntelliJ IDEA:
1. Import the project as a Maven project
2. Set up JDK 17
3. Create TestNG run configurations
4. Run tests directly from IDE

## 📊 Reports

### ExtentReports

- **Location**: `reports/` directory
- **Format**: HTML
- **Features**: Screenshots, test timeline, categorized results

### Allure Reports

```bash
# Generate and serve Allure reports
allure serve allure-results

# Generate static HTML report
allure generate allure-results --clean
```

### Report Features

- **Test Execution Timeline**: Visual timeline of test execution
- **Screenshots on Failure**: Automatic capture and attachment
- **Detailed Logs**: Comprehensive logging with different levels
- **Test History**: Track test results over time
- **Categorized Results**: Grouped by test status and categories

## 🌍 Environment Setup

### Development Environment

1. **Local Setup**:
   ```bash
   mvn test -P local
   ```

2. **SIT Environment**:
   ```bash
   mvn test -P sit
   ```

3. **UAT Environment**:
   ```bash
   mvn test -P uat
   ```

### CI/CD Integration

#### GitHub Actions Example

```yaml
name: Run Tests
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
        distribution: 'adopt'
    - name: Run tests
      run: mvn test
    - name: Generate Allure Report
      run: allure generate allure-results --clean
```

#### Jenkins Pipeline Example

```groovy
pipeline {
    agent any
    tools {
        maven 'Maven 3.8'
        jdk 'JDK 17'
    }
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Report') {
            steps {
                sh 'allure generate allure-results --clean'
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'allure-report',
                    reportFiles: 'index.html',
                    reportName: 'Allure Report'
                ])
            }
        }
    }
}
```

## 🤝 Contributing

### Development Guidelines

1. **Follow Page Object Model**: Use page objects for all web interactions
2. **Use Descriptive Names**: Clear and meaningful method and variable names
3. **Add Logging**: Include meaningful logs for debugging
4. **Write Tests**: Add unit tests for new utilities
5. **Update Documentation**: Keep README and documentation updated

### Code Style

- Use Java conventions
- Add JavaDoc comments for public methods
- Follow the established package structure
- Use proper exception handling

### Pull Request Process

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Update documentation
6. Submit a pull request

## 🔧 Troubleshooting

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

#### Permission Issues

```bash
# On Unix systems, ensure proper permissions
chmod +x mvnw
```

### Debug Mode

Enable debug logging in `src/test/resources/log4j2.xml`:

```xml
<Root level="debug">
    <AppenderRef ref="Console"/>
    <AppenderRef ref="File"/>
</Root>
```

### Getting Help

1. Check the [documentation](Trident_Automation_Framework_Documentation.md)
2. Review test logs in the `logs/` directory
3. Examine screenshots in `screenshots/` for failed tests
4. Contact the framework development team

## 📚 Documentation

- **[Complete Framework Documentation](Trident_Automation_Framework_Documentation.md)** - Comprehensive guide
- **[API Documentation](docs/api/)** - API testing guide
- **[Page Object Guide](docs/page-objects/)** - Page object model best practices

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🏆 Acknowledgments

- **Playwright Team** - For the excellent browser automation library
- **TestNG Team** - For the robust testing framework
- **ExtentReports** - For the beautiful reporting solution
- **Allure** - For the advanced test reporting capabilities

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the framework development team
- Check the troubleshooting section above

---

**Version**: 1.0  
**Last Updated**: February 2025  
**Framework**: Trident Automation Framework

---

⭐ **Star this repository if it helps you!** ⭐
