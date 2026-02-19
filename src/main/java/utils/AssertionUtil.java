package utils;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

/**
 * Centralized assertion utility for TestNG framework.
 * Provides:
 * - Hard assertions (immediate test failure)
 * - Soft assertions (collect failures and report at end)
 * - Thread-safe SoftAssert handling for parallel execution
 * This helps maintain:
 * - Consistent error messages
 * - Cleaner test classes
 * - Better reporting structure
 */

public class AssertionUtil {

    /**
     * ThreadLocal SoftAssert to support parallel execution safely.
     */

    private static final ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

    /**
     * Hard assert equals.
     *
     * @param actual   Actual value
     * @param expected Expected value
     * @param message  Assertion message
     */

    public static void assertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message + " | Expected: [" + expected + "] but found: [" + actual + "]");
    }

    /**
     * Hard assert true.
     */

    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message + " | Condition evaluated to FALSE");
    }

    /**
     * Hard assert false.
     */

    public static void assertFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message + " | Condition evaluated to TRUE");
    }

    /**
     * Hard assert not null.
     */

    public static void assertNotNull(Object object, String message) {
        Assert.assertNotNull(object, message + " | Object was NULL");
    }

    /**
     * Force test failure.
     */

    public static void fail(String message) {
        Assert.fail(message);
    }

    /**
     * Soft assert equals.
     */

    public static void softAssertEquals(Object actual, Object expected, String message) {
        softAssert.get().assertEquals(actual, expected, message + " | Expected: [" + expected + "] but found: [" + actual + "]");
    }

    /**
     * Soft assert true.
     */

    public static void softAssertTrue(boolean condition, String message) {
        softAssert.get().assertTrue(condition, message + " | Condition evaluated to FALSE");
    }

    /**
     * Soft assert false.
     */

    public static void softAssertFalse(boolean condition, String message) {
        softAssert.get().assertFalse(condition, message + " | Condition evaluated to TRUE");
    }

    /**
     * Soft assert not null.
     */

    public static void softAssertNotNull(Object object, String message) {
        softAssert.get().assertNotNull(object, message + " | Object was NULL");
    }

    /**
     * Executes all collected soft assertions.
     * Must be called at the end of test method
     * to evaluate all soft assertion failures.
     * Clears ThreadLocal after execution.
     */
    public static void assertAll() {
        softAssert.get().assertAll();
        softAssert.remove();
    }
}
