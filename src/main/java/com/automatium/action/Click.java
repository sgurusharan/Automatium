package com.automatium.action;

import com.automatium.page.BasePage;
import org.openqa.selenium.By;

/**
 * Created by Gurusharan on 26-11-2016.
 */
public class Click implements UIAction {
    private By locator = null;
    private String locatorKey = null;

    /**
     * Creates a UIAction to click on an element located
     * by the given locator when invoked.
     *
     * @param locator
     */
    public Click(By locator) {
        this.locator = locator;
    }

    /**
     * Creates a UIAction to click on the element found using
     * the locator key from current page's locator map. The
     * current page is determined before invoking the UIAction.
     *
     * @param locatorKey
     */
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
