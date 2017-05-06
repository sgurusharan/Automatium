package com.automatium.utils;

import org.junit.Assert;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class TestUtils {

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
