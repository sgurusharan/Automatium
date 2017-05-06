package com.automatium.action;

import com.automatium.page.BasePage;
import org.openqa.selenium.By;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class SelectOption implements UIAction {
    private By locator = null;
    private String locatorKey = null;
    private SelectBy selectBy;
    private String selectValue;

    private SelectOption(SelectBy selectBy, String selectValue) {
        this.selectBy = selectBy;
        this.selectValue = selectValue;
    }
    public SelectOption(By locator, SelectBy selectBy, String selectValue) {
        this(selectBy, selectValue);
        this.locator = locator;
    }

    public SelectOption(String locatorKey, SelectBy selectBy, String selectValue) {
        this(selectBy, selectValue);
        this.locatorKey = locatorKey;
    }

    public SelectOption(By locator, int indexToSelect) {
        this(locator, SelectBy.INDEX, Integer.toString(indexToSelect));
    }

    public SelectOption(String locatorKey, int indexToSelect) {
        this(locatorKey, SelectBy.INDEX, Integer.toString(indexToSelect));
    }

    public SelectOption(By locator, String displayedString) {
        this(locator, SelectBy.DISPLAYED_STRING, displayedString);
    }

    public SelectOption(String locatorKey, String displayedString) {
        this(locatorKey, SelectBy.DISPLAYED_STRING, displayedString);
    }

    public By getLocator(BasePage page) {
        if (locator == null) {
            locator = page.getLocator(locatorKey);
        }
        return locator;
    }

    public SelectBy getSelectBy() {
        return selectBy;
    }

    public String getSelectValue() {
        return selectValue;
    }

    public enum SelectBy {
        DISPLAYED_STRING,
        VALUE,
        INDEX
    }
}
