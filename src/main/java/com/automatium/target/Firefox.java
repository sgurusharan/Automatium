package com.automatium.target;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class Firefox extends TestTarget {

    @Override
    public WebDriver getAsWebDriver() {
        return new FirefoxDriver();
    }

    @Override
    public String toString() {
        return "Mozilla Firefox";
    }
}
