package com.automatium.target;

import com.automatium.exception.UnknownTargetException;
import org.openqa.selenium.WebDriver;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public abstract class TestTarget {
    public abstract WebDriver getAsWebDriver();

    public static final String CHROME_TEST_TARGET = "chrome";
    public static final String SAFARI_TEST_TARGET = "safari";
    public static final String FIREFOX_TEST_TARGET = "firefox";
    public static final String IE_TEST_TARGET = "ie";

    public static TestTarget testTargetFromString(String testTargetString) {
        TestTarget toReturn;
        switch (testTargetString) {
            case CHROME_TEST_TARGET:
                toReturn = new Chrome();
                break;
            case SAFARI_TEST_TARGET:
                toReturn = new Safari();
                break;
            case FIREFOX_TEST_TARGET:
                toReturn = new Firefox();
                break;
            case IE_TEST_TARGET:
                toReturn = new InternetExplorer();
                break;
            default:
                throw new UnknownTargetException(testTargetString);
        }

        return toReturn;
    }
}
