package com.automatium.target;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class Chrome extends TestTarget {

    private boolean isFirstDriver = true;

    public void setupDriverManager() {
        ChromeDriverManager.getInstance().setup();
    }

    @Override
    public WebDriver getAsWebDriver() {
        if (isFirstDriver) {
            setupDriverManager();
            isFirstDriver = false;
        }
        return new ChromeDriver();
    }

    @Override
    public String toString() {
        return "Google Chrome";
    }
}
