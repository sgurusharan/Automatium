package com.automatium.utils;

import org.junit.Assert;

/**
 * Created by Gurusharan on 27-11-2016.
 *
 * Generic utilities for various test activities.
 *
 */
public class TestUtils {

    /**
     * A single method interface to specify a
     * custom satisfaction condition.
     */
    public interface CustomExpectedCondition {
        boolean isSatisfied();
    }

    /**
     * Wait until the given CustomExpectedCondition is satisfied for
     * a maximum of given number of seconds.
     *
     * @param timeoutInSeconds
     * @param condition
     * @return true if the condition was satisfied; false otherwise.
     */
    public static boolean waitForCondition(Long timeoutInSeconds, CustomExpectedCondition condition) {
        Long elapsed = 0L;
        while (!condition.isSatisfied() && elapsed < timeoutInSeconds) {
            elapsed++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Do nothing
            }
        }
        return condition.isSatisfied();
    }

    /**
     * Shorthand method to call JUnit's assertTrue()
     *
     * @param value
     * @param message
     */
    public static void assertTrue(boolean value, String message) {
        Assert.assertTrue(message, value);
    }

    /**
     * Shorthand method to call JUnit's fail() - to cause an immediate test failure.
     *
     * @param message
     */
    public static void assertFail(String message) {
        Assert.fail(message);
    }

    /**
     * Shorthand method to call JUnit's assertFalse()
     *
     * @param value
     * @param message
     */
    public static void assertFalse(boolean value, String message) {
        Assert.assertFalse(message, value);
    }
}
