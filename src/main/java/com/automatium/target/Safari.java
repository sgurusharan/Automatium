package com.automatium.target;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class Safari extends TestTarget {

    @Override
    public WebDriver getAsWebDriver() {
        return new SafariDriver();
    }

    @Override
    public String toString() {
        return "Apple Safari";
    }
}
