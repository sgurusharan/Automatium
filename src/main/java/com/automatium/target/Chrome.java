package com.automatium.target;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class Chrome extends TestTarget {

    @Override
    public WebDriver getAsWebDriver() {
        return new ChromeDriver();
    }

    @Override
    public String toString() {
        return "Google Chrome";
    }
}
