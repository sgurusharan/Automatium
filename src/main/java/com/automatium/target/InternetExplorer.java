package com.automatium.target;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class InternetExplorer extends TestTarget {

    @Override
    public WebDriver getAsWebDriver() {
        return new InternetExplorerDriver();
    }

    @Override
    public String toString() {
        return "Microsoft Internet Explorer";
    }
}
