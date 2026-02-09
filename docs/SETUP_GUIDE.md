# Trident Framework Setup Guide

## Overview

This guide provides step-by-step instructions for setting up the Trident Automation Framework on your local machine and configuring it for different environments.

## Prerequisites

### System Requirements

- **Operating System**: Windows 10/11, macOS 10.15+, or Linux (Ubuntu 18.04+)
- **Java**: JDK 17 or higher
- **Maven**: 3.6.0 or higher
- **Git**: Latest version
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code (recommended)

### Browser Requirements

- **Google Chrome**: Latest version (for Chromium)
- **Mozilla Firefox**: Latest version (optional)
- **Safari**: Latest version (macOS only, for WebKit)

## Installation Steps

### 1. Java Development Kit (JDK)

#### Windows
```bash
# Download JDK 17 from Oracle or OpenJDK
# https://adoptium.net/temurin/releases/?version=17

# Verify installation
java -version
javac -version
```

#### macOS
```bash
# Using Homebrew
brew install openjdk@17

# Set JAVA_HOME
echo 'export JAVA_HOME=/usr/local/opt/openjdk@17' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

#### Linux (Ubuntu)
```bash
sudo apt update
sudo apt install openjdk-17-jdk

# Verify installation
java -version
```

### 2. Apache Maven

#### Windows
1. Download Maven from https://maven.apache.org/download.cgi
2. Extract to `C:\Program Files\Apache\maven`
3. Add to PATH:
   ```
   C:\Program Files\Apache\maven\bin
   ```
4. Set environment variables:
   ```
   MAVEN_HOME=C:\Program Files\Apache\maven
   ```

#### macOS
```bash
brew install maven
```

#### Linux
```bash
sudo apt install maven
```

### 3. Git

#### Windows
Download and install from https://git-scm.com/download/win

#### macOS
```bash
brew install git
```

#### Linux
```bash
sudo apt install git
```

### 4. IDE Setup (Optional but Recommended)

#### IntelliJ IDEA
1. Download IntelliJ IDEA Community or Ultimate
2. Install Java and Maven plugins (usually included)
3. Configure JDK and Maven in File → Project Structure

#### Eclipse
1. Download Eclipse IDE for Enterprise Java Developers
2. Install Maven Integration for Eclipse (m2e)
3. Configure JDK in Window → Preferences → Java → Installed JREs

## Framework Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd tridentAutomation
```

### 2. Install Dependencies

```bash
mvn clean install
```

This will:
- Download all Maven dependencies
- Compile the source code
- Run tests (if any)
- Install the framework to local Maven repository

### 3. Verify Installation

```bash
mvn test
```

If the setup is correct, you should see tests executing successfully.

## Configuration

### 1. Environment Configuration

The framework supports multiple environments through Maven profiles. Configure your environment in `src/test/resources/config.properties`.

#### Basic Configuration

```properties
# Browser Settings
browser=chrome
headless=false

# Common Settings
authToken=your_auth_token_here
timeSeriesPathURL=/query/api/kpis/timeseries
rawParameterPathURL=/query/api/raw-parameters/timeseries
aggregatePathURL=/query/api/kpis/aggregates
```

#### SIT Environment Configuration

```properties
# SIT Environment
sit.webUrl=https://sit-process.infinite-uptime.com/dashboard?q=&view=compact
sit.APIBaseURL=https://sit-ipf.infinite-uptime.com
sit.userName=your_sit_username@domain.com
sit.webPassword=your_sit_password
sit.X-ORG-ID=901
```

#### UAT Environment Configuration

```properties
# UAT Environment
uat.webUrl=https://uat-new-process.infinite-uptime.com/dashboard?q=&view=compact
uat.APIBaseURL=https://uat-new-ipf.infinite-uptime.com
uat.userName=your_uat_username@domain.com
uat.webPassword=your_uat_password
uat.X-ORG-ID=1138
```

### 2. Browser Configuration

#### Supported Browsers

- **Chrome** (default): `browser=chrome`
- **Firefox**: `browser=firefox`
- **WebKit/Safari**: `browser=webkit`

#### Headless Mode

```properties
# Run tests without visible browser
headless=true

# Run tests with visible browser (useful for debugging)
headless=false
```

### 3. Logging Configuration

Configure logging in `src/test/resources/log4j2.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
        <File name="File" fileName="logs/test.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n"/>
        </File>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="File"/>
        </Root>
    </Loggers>
</Configuration>
```

### 4. TestNG Configuration

Configure test execution in `testng.xml`:

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="PlaywrightJava" parallel="tests" thread-count="2">
    <listeners>
        <listener class-name="listeners.Listeners"/>
    </listeners>
    <test name="PlaywrightTrident">
        <classes>
            <class name="test.web.AdminFlowTest"/>
        </classes>
    </test>
</suite>
```

## Running Tests

### 1. Run All Tests

```bash
mvn test
```

### 2. Run with Specific Profile

```bash
# SIT Environment
mvn test -P sit

# UAT Environment
mvn test -P uat
```

### 3. Run Specific Test Class

```bash
mvn test -Dtest=AdminFlowTest
```

### 4. Run Specific Test Method

```bash
mvn test -Dtest=AdminFlowTest#testAdminFlow
```

### 5. Run Tests in Parallel

Configure parallel execution in `testng.xml`:

```xml
<suite name="PlaywrightJava" parallel="tests" thread-count="4">
    <!-- Test configuration -->
</suite>
```

## Reporting

### 1. Allure Reports

#### Install Allure Commandline

#### Windows
```bash
# Download from https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/
# Extract and add to PATH
```

#### macOS
```bash
brew install allure
```

#### Linux
```bash
# Download and extract
sudo apt install allure-commandline
```

#### Generate Reports

```bash
# Run tests
mvn test

# Generate and serve Allure report
allure serve target/allure-results

# Or generate static report
allure generate target/allure-results -o target/allure-report
```

### 2. ExtentReports

ExtentReports are generated automatically and can be found in the `reports/` directory after test execution.

## Troubleshooting

### Common Issues

#### 1. Java Version Issues

**Problem**: `UnsupportedClassVersionError`

**Solution**: Ensure you're using Java 17 or higher:

```bash
java -version
javac -version
```

#### 2. Maven Dependency Issues

**Problem**: `Could not resolve dependencies`

**Solution**: Clean and reinstall:

```bash
mvn clean
mvn dependency:resolve
mvn install
```

#### 3. Browser Launch Issues

**Problem**: `Browser failed to launch`

**Solution**: 
- Check browser installation
- Verify browser driver compatibility
- Try running in headless mode

#### 4. Authentication Issues

**Problem**: `401 Unauthorized` API responses

**Solution**:
- Verify auth token in `config.properties`
- Check token expiration
- Validate organization ID

#### 5. Port Conflicts

**Problem**: `Port already in use`

**Solution**:
- Identify and kill processes using the port
- Configure different ports in test configuration

### Debug Mode

Enable debug logging for troubleshooting:

1. **Update log4j2.xml**:
```xml
<Root level="debug">
    <AppenderRef ref="Console"/>
    <AppenderRef ref="File"/>
</Root>
```

2. **Run with debug options**:
```bash
mvn test -Dmaven.surefire.debug
```

3. **Enable Playwright debug**:
```bash
# Set environment variable
set DEBUG=pw:api
mvn test
```

## IDE Configuration

### IntelliJ IDEA

1. **Import Project**:
   - File → Open → Select `pom.xml`
   - Open as Project

2. **Configure SDK**:
   - File → Project Structure → Project Settings → Project
   - Set Project SDK to JDK 17

3. **Configure Maven**:
   - File → Settings → Build Tools → Maven
   - Set Maven home directory

4. **Run Configuration**:
   - Run → Edit Configurations
   - Add Maven configuration with goal `test`

### Eclipse

1. **Import Project**:
   - File → Import → Maven → Existing Maven Projects
   - Select project directory

2. **Configure JDK**:
   - Window → Preferences → Java → Installed JREs
   - Add JDK 17

3. **Run Tests**:
   - Right-click on test class → Run As → TestNG Test

### VS Code

1. **Install Extensions**:
   - Extension Pack for Java
   - Test Runner for Java

2. **Open Project**:
   - File → Open Folder → Select project directory

3. **Run Tests**:
   - Use Test Runner extension or integrated terminal

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
        
    - name: Cache Maven dependencies
      uses: actions/cache@v2
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        
    - name: Run tests
      run: mvn clean test -P uat
      
    - name: Generate Allure Report
      uses: simple-elf/allure-report-action@master
      if: always()
      with:
        allure-results: target/allure-results
```

## Performance Optimization

### 1. Parallel Execution

Configure parallel test execution in `testng.xml`:

```xml
<suite name="PlaywrightJava" parallel="tests" thread-count="4">
    <test name="WebTests">
        <classes>
            <class name="test.web.AdminFlowTest"/>
            <class name="test.web.DashboardTest"/>
        </classes>
    </test>
    <test name="APITests">
        <classes>
            <class name="test.api.GetKpiData"/>
            <class name="test.api.GetEquipment"/>
        </classes>
    </test>
</suite>
```

### 2. Maven Optimization

```bash
# Skip tests during build
mvn clean install -DskipTests

# Run tests only
mvn test

# Parallel Maven execution
mvn -T 4 clean test
```

### 3. Browser Optimization

```properties
# Use lightweight browser options
browser=chrome
headless=true

# Optimize browser launch arguments
# (configured in PlaywrightFactory)
```

## Security Considerations

### 1. Credential Management

- **Never commit credentials to version control**
- Use environment variables for sensitive data
- Consider using encrypted properties

### 2. Environment Variables

```bash
# Set environment variables
export TRIDENT_AUTH_TOKEN="your_token_here"
export TRIDENT_ORG_ID="your_org_id"
export TRIDENT_PASSWORD="your_password"

# Use in config.properties
authToken=${TRIDENT_AUTH_TOKEN}
X-ORG-ID=${TRIDENT_ORG_ID}
webPassword=${TRIDENT_PASSWORD}
```

### 3. Secure Configuration

Consider using tools like:
- **Vault** for secret management
- **Jasypt** for property encryption
- **Environment-specific configuration files**

---

This setup guide should help you get the Trident Framework running smoothly in your environment. For additional support, refer to the troubleshooting section or create an issue in the project repository.
