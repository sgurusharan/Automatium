package com.automatium.action;

import com.automatium.page.BasePage;
import org.openqa.selenium.By;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class TypeText implements UIAction {
    private By locator = null;
    private String locatorKey = null;
    private String text;

    private TypeText(String text) {
        this.text = text;
    }

    public TypeText(By locator, String text) {
        this(text);
        this.locator = locator;
    }

    public TypeText(String locatorKey, String text) {
        this(text);
        this.locatorKey = locatorKey;
    }

    public By getLocator(BasePage page) {
        if (locator == null) {
            locator = page.getLocator(locatorKey);
        }
        return locator;
    }

    public String getText() {
        return text;
    }
}
