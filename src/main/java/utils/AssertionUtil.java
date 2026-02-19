package utils;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class AssertionUtil {

    private static final ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

    public static void assertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message + " | Expected: [" + expected + "] but found: [" + actual + "]");
    }

    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message + " | Condition evaluated to FALSE");
    }

    public static void assertFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message + " | Condition evaluated to TRUE");
    }

    public static void assertNotNull(Object object, String message) {
        Assert.assertNotNull(object, message + " | Object was NULL");
    }

    public static void fail(String message) {
        Assert.fail(message);
    }

    public static void softAssertEquals(Object actual, Object expected, String message) {
        softAssert.get().assertEquals(actual, expected, message + " | Expected: [" + expected + "] but found: [" + actual + "]");
    }

    public static void softAssertTrue(boolean condition, String message) {
        softAssert.get().assertTrue(condition, message + " | Condition evaluated to FALSE");
    }

    public static void softAssertFalse(boolean condition, String message) {
        softAssert.get().assertFalse(condition, message + " | Condition evaluated to TRUE");
    }

    public static void softAssertNotNull(Object object, String message) {
        softAssert.get().assertNotNull(object, message + " | Object was NULL");
    }
    public static void assertAll() {
        softAssert.get().assertAll();
        softAssert.remove();
    }
}
