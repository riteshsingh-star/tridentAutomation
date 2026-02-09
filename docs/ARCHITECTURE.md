# Trident Framework Architecture

## Overview

The Trident Automation Framework follows a layered architecture pattern that promotes separation of concerns, maintainability, and scalability. This document outlines the architectural decisions, design patterns, and component interactions.

## Architectural Principles

### 1. Separation of Concerns
- **Test Logic**: Business logic and test scenarios
- **Page Objects**: UI element locators and interactions
- **Utilities**: Reusable helper functions
- **Configuration**: Environment-specific settings
- **Data Management**: Test data handling and parsing

### 2. Thread Safety
- ThreadLocal storage for browser instances
- Isolated test execution environments
- Parallel test execution support

### 3. Extensibility
- Plugin-based architecture for listeners
- Configurable reporting mechanisms
- Modular utility functions

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    Test Layer                               │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   Web Tests     │  │   API Tests     │  │  Integration │ │
│  │   (BaseTest)    │  │   (APIBase)     │  │   Tests      │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                  Page Object Layer                          │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   BasePage      │  │   LoginPage     │  │  Dashboard   │ │
│  │   (Common)      │  │   (Specific)    │  │  (Specific)  │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                    Service Layer                           │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │ PlaywrightFactory│ │  API Services   │  │  Data Utils  │ │
│  │                 │  │                 │  │              │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                   Utility Layer                            │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │  WaitUtils      │  │  Config Utils   │  │  JSON Utils  │ │
│  │                 │  │                 │  │              │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                  Configuration Layer                       │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │ config.properties│ │   log4j2.xml    │  │  testng.xml  │ │
│  │                 │  │                 │  │              │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## Design Patterns

### 1. Factory Pattern

**PlaywrightFactory** implements the Factory pattern to create and manage browser instances:

```java
public class PlaywrightFactory {
    protected static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    protected static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    protected static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    protected static ThreadLocal<Page> tlPage = new ThreadLocal<>();
    
    public static void initBrowser(String browserName, boolean headless) {
        // Factory method to create browser instances
    }
}
```

**Benefits:**
- Centralized browser creation logic
- Thread-safe browser management
- Easy switching between browser types

### 2. Page Object Model (POM)

**BasePage** serves as the abstract base for all page objects:

```java
public class BasePage {
    public Page page;
    public BrowserContext context;
    
    // Common element interaction methods
    public Locator getByRoleButton(String text, Page page) { ... }
    public Locator getByText(String text, Page page) { ... }
    public static void waitAndClick(Page page, Locator locator, int waitMs) { ... }
}
```

**Benefits:**
- Reusable UI interaction methods
- Consistent element location strategies
- Reduced code duplication

### 3. Template Method Pattern

**BaseTest** defines the template for test execution:

```java
public class BaseTest {
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        // Template method defining setup sequence
        log.info("===== Test Setup Started =====");
        String browserName = ReadPropertiesFile.get("browser");
        PlaywrightFactory.initBrowser(browserName, headless);
        PlaywrightFactory.createContextAndPage();
        page = PlaywrightFactory.getPage();
        context = PlaywrightFactory.getContext();
        page.navigate(baseUrl);
        performLogin();
    }
}
```

**Benefits:**
- Consistent test setup/teardown
- Enforced execution sequence
- Customizable hook points

### 4. Singleton Pattern (Configuration)

**ReadPropertiesFile** implements singleton-like behavior for configuration:

```java
public class ReadPropertiesFile {
    private static Properties prop;
    
    public static String get(String key) {
        // Singleton access to configuration
    }
}
```

**Benefits:**
- Single configuration source
- Memory efficient
- Thread-safe access

## Component Interactions

### Test Execution Flow

1. **Test Initialization**
   ```
   TestNG → BaseTest.setUp() → PlaywrightFactory.initBrowser()
   ```

2. **Page Interaction**
   ```
   Test Method → Page Object → BasePage → Playwright API
   ```

3. **API Testing**
   ```
   Test Method → APIBase → Playwright API Request
   ```

4. **Reporting**
   ```
   TestNG Listener → ExtentManager → Allure Reports
   ```

### Data Flow

```
External Data → JSON/Properties → Utils → Test Methods → Reports
```

## Thread Safety Implementation

### ThreadLocal Usage

The framework uses ThreadLocal to ensure thread safety:

```java
protected static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
protected static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
protected static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
protected static ThreadLocal<Page> tlPage = new ThreadLocal<>();
```

**Isolation Strategy:**
- Each thread gets its own browser instance
- Separate contexts prevent cross-test interference
- Automatic cleanup after test completion

## Configuration Management

### Environment-Based Configuration

```xml
<!-- Maven Profiles -->
<profiles>
    <profile>
        <id>sit</id>
        <properties>
            <env>sit</env>
        </properties>
    </profile>
    <profile>
        <id>uat</id>
        <properties>
            <env>uat</env>
        </properties>
    </profile>
</profiles>
```

### Property Hierarchy

1. **System Properties** (highest priority)
2. **Environment Variables**
3. **config.properties**
4. **Default Values** (lowest priority)

## Error Handling Strategy

### Exception Hierarchy

```
RuntimeException
├── FrameworkException
│   ├── BrowserInitializationException
│   ├── ElementNotFoundException
│   └── APIRequestException
└── TestExecutionException
```

### Recovery Mechanisms

1. **Automatic Retry**: Configurable retry for failed tests
2. **Screenshot Capture**: On failure, capture page state
3. **Detailed Logging**: Comprehensive error context
4. **Graceful Degradation**: Continue test suite on individual failures

## Performance Considerations

### Optimization Strategies

1. **Lazy Initialization**: Browser instances created only when needed
2. **Resource Pooling**: Reuse browser contexts where possible
3. **Parallel Execution**: Maximize resource utilization
4. **Caching**: Cache frequently accessed data

### Memory Management

```java
@AfterClass(alwaysRun = true)
public void tearDown() {
    PlaywrightFactory.closeContext();
    PlaywrightFactory.closeBrowser();
    // Explicit cleanup to prevent memory leaks
}
```

## Extensibility Points

### Custom Listeners

```java
public class Listeners implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        // Custom test start logic
    }
}
```

### Utility Extensions

New utilities can be added by extending base classes or implementing interfaces:

```java
public class CustomWaitUtils extends WaitUtils {
    public static void waitForCustomCondition(Locator element, Condition condition) {
        // Custom wait implementation
    }
}
```

## Security Considerations

### Credential Management

1. **Environment Variables**: Sensitive data in environment variables
2. **Encrypted Properties**: Option for encrypted configuration
3. **Token Refresh**: Automatic API token renewal
4. **Secure Storage**: Avoid hardcoded credentials

### Data Protection

1. **Input Sanitization**: Validate all test inputs
2. **Secure Communication**: HTTPS for all API calls
3. **Data Masking**: Sensitive data in logs and reports

## Monitoring and Observability

### Logging Strategy

```xml
<!-- Log4j2 Configuration -->
<Configuration>
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
        <File name="File" fileName="logs/test.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n"/>
        </File>
    </Appenders>
</Configuration>
```

### Metrics Collection

- Test execution time
- Browser performance metrics
- API response times
- Resource utilization

## Future Architecture Considerations

### Scalability

1. **Distributed Testing**: Grid-based execution
2. **Cloud Integration**: Browser services integration
3. **Microservices**: Modular service architecture

### Technology Evolution

1. **AI Integration**: Smart test generation
2. **Visual Testing**: Image comparison capabilities
3. **Mobile Testing**: Cross-platform support

---

This architecture document serves as a guide for understanding the framework's design decisions and provides a foundation for future enhancements and maintenance.
