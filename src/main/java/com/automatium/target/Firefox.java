package com.automatium.target;

import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class Firefox extends TestTarget {
    private boolean isFirstDriver = true;

    public void setupDriverManager() {
        FirefoxDriverManager.getInstance().setup();
    }

    @Override
    public WebDriver getAsWebDriver() {
        if (isFirstDriver) {
            setupDriverManager();
            isFirstDriver = false;
        }
        return new FirefoxDriver();
    }

    @Override
    public String toString() {
        return "Mozilla Firefox";
    }
}
