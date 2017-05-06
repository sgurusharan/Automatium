package com.automatium.utils;

import com.automatium.logging.TestLogger;
import org.reflections.Reflections;

/**
 * Created by Gurusharan on 26-11-2016.
 */
public class GlobalUtils {
    public static final Reflections reflections = new Reflections();
    public static final Long ONE_SECOND_IN_MILLIS = 1000L;

    private static final TestLogger logger = TestLogger.getSingletonInstance();

    public static void wait(Long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.error("UIACTION", "Unable to wait: " + e);
        }
    }
}
