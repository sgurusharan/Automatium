package com.automatium.action;

import com.automatium.page.BasePage;
import org.openqa.selenium.By;

/**
 * Created by Gurusharan on 26-11-2016.
 */
public class Click implements UIAction {
    private By locator = null;
    private String locatorKey = null;

    public Click(By locator) {
        this.locator = locator;
    }

    public Click(String locatorKey) {
        this.locatorKey = locatorKey;
    }

    public By getLocator(BasePage page) {
        if (locator == null) {
            locator = page.getLocator(locatorKey);
        }
        return locator;
    }
}
