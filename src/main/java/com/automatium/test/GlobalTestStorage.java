package com.automatium.test;

import com.automatium.utils.SeleniumUtils;
import org.openqa.selenium.WebDriver;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class GlobalTestStorage {
    private static WebDriver globalDriver = SeleniumUtils.getNewWebDriver();

    static WebDriver getGlobalDriver() {
        return globalDriver;
    }
}
