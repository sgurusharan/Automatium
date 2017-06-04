package com.automatium.target;

import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class InternetExplorer extends TestTarget {
    private boolean isFirstDriver = true;

    public void setupDriverManager() {
        InternetExplorerDriverManager.getInstance().setup();
    }

    @Override
    public WebDriver getAsWebDriver() {
        if (isFirstDriver) {
            setupDriverManager();
            isFirstDriver = false;
        }
        return new InternetExplorerDriver();
    }

    @Override
    public String toString() {
        return "Microsoft Internet Explorer";
    }
}
