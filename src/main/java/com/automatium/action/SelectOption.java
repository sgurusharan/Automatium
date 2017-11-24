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

    /**
     * Creates a UIAction that selects an option in a list box or dropdown
     * located by the given locator list by the index of the option (that
     * starts from 0)
     *
     * @param locator
     * @param indexToSelect
     */
    public SelectOption(By locator, int indexToSelect) {
        this(locator, SelectBy.INDEX, Integer.toString(indexToSelect));
    }

    /**
     * Creates a UIAction that selects an option in a list box or dropdown
     * located using the given key in the page's locator map, based on the
     * index of the option (that starts from 0)
     *
     * @param locatorKey
     * @param indexToSelect
     */
    public SelectOption(String locatorKey, int indexToSelect) {
        this(locatorKey, SelectBy.INDEX, Integer.toString(indexToSelect));
    }

    /**
     * Creates a UIAction that selects the given option in a list box or
     * dropdown located using the given selector.
     *
     * @param locator
     * @param displayedString
     */
    public SelectOption(By locator, String displayedString) {
        this(locator, SelectBy.DISPLAYED_STRING, displayedString);
    }

    /**
     * Creates a UIAction that selects the given option in a list box or
     * dropdown located using the given key from the page's locator map.
     *
     * @param locatorKey
     * @param displayedString
     */
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
