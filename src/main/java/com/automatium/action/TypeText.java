package com.automatium.action;

import com.automatium.page.BasePage;
import org.openqa.selenium.By;

/**
 * Created by Gurusharan on 27-11-2016.
 *
 * A UIAction for typing text into a text field or text area.
 * This can also be used for inserting file paths into file input fields.
 */
public class TypeText implements UIAction {
    private By locator = null;
    private String locatorKey = null;
    private String text;

    private TypeText(String text) {
        this.text = text;
    }

    /**
     * Creates an action that types the given text, into
     * the WebElement located by the given locator when
     * invoked.
     *
     * @param locator
     * @param text
     */
    public TypeText(By locator, String text) {
        this(text);
        this.locator = locator;
    }

    /**
     * Creates an action that types the given text, into
     * the WebElement identified by the given locator key
     * in the page's locator map, when invoked. The page
     * is dynamically determined before invoking the UI
     * action.
     *
     * @param locatorKey
     * @param text
     */
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
