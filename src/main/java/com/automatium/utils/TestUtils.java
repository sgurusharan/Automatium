package com.automatium.utils;

import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class TestUtils {

    public interface CustomExpectedCondition {
        boolean isSatisfied();
    }

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

    public static void assertTrue(boolean value, String message) {
        Assert.assertTrue(message, value);
    }

    public static void assertFail(String message) {
        Assert.fail(message);
    }

    public static void assertFalse(boolean value, String message) {
        Assert.assertFalse(message, value);
    }
}
