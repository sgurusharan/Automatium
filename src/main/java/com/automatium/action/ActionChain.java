package com.automatium.action;

import com.automatium.file.FileHandler;
import com.automatium.logging.TestLogger;
import com.automatium.page.BasePage;
import org.openqa.selenium.By;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class ActionChain {

    private List<UIAction> actions = new LinkedList<>();
    private BasePage currentPage;

    /**
     * Used to create an ActionChain starting from the passed page.
     * Use ActionChain.start(page) instead.
     *
     * @param currentPage
     */
    public ActionChain(BasePage currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Starts a new ActionChain starting from passed page.
     *
     * @param currentPage
     * @return an ActionChain object that can be used to build UIAction chains.
     */
    public static ActionChain start(BasePage currentPage) {
        return new ActionChain(currentPage);
    }

    /**
     * Add a Click UIAction to the ActionChain.
     *
     * @param locator
     * @return the current ActionChain (for method chaining)
     */
    public ActionChain click(By locator) {
        addAction(new Click(locator));
        return this;
    }

    /**
     * Adds a list of PageActions or AssertPageActions loaded from a file
     * to the current action chain.
     *
     * CSV and Excel files are supported as of now.
     *
     * The first column of the file is considered as the PageAction or
     * AssertPageAction and the remaining columns will be used as parameters
     * to the page action.
     *
     * If the first column ends with a '?' then it is considered as an
     * AssertPageAction. If not, it is considered as a PageAction.
     *
     * @param filePath
     * @return the current ActionChain (for method chaining)
     */
    public ActionChain fromFile(String filePath) {

        TestLogger.getSingletonInstance().debug("TESTSETUP", "Loading PageActions from file " + filePath);

        FileHandler fileHandler = FileHandler.getFileHandlerForFile(filePath);

        int i = 0; // For logging

        while (true) {
            FileHandler.ActionEntry actionEntry = fileHandler.getNextAction();

            if (actionEntry == null) {
                break;
            }

            if (actionEntry.getAction().endsWith("?")) {
                // Generally speaking page actions cannot have '?' (as they are supposed to be valid Java methods.
                // So, it doesn't matter if I do a replaceAll here instead of replacing only the last occurrence.
                assertPageAction(actionEntry.getAction().replaceAll("\\?", ""), actionEntry.getArguments().toArray(new String[0]));
            }
            else {
                pageAction(actionEntry.getAction(), actionEntry.getArguments().toArray(new String[0]));
            }

            i++;
        }

        TestLogger.getSingletonInstance().debug("TESTSETUP", "Loaded " + i + " PageActions ");

        return this;
    }

    /**
     * Returns all the UIAction objects added to the current
     * ActionChain.
     *
     * @return a list of UIAction objects
     */
    public List<UIAction> getActions() {
        return actions;
    }

    /**
     * Adds a TypeText UIAction to the current ActionChain.
     *
     * @param locator
     * @param text
     * @return the current ActionChain (for method chaining)
     */
    public ActionChain type(By locator, String text) {
        addAction(new TypeText(locator, text));
        return this;
    }

    /**
     * Adds a SelectOption UIAction based on the given
     * select by clause to the current ActionChain.
     *
     * @param locator
     * @param selectBy
     * @param value
     * @return the current ActionChain (for method chaining)
     */
    public ActionChain select(By locator, SelectOption.SelectBy selectBy, String value) {
        addAction(new SelectOption(locator, selectBy, value));
        return this;
    }

    /**
     * Adds a SelectOption UIAction based on option index
     * to the current ActionChain.
     *
     * @param locator
     * @param indexToSelect
     * @return the current ActionChain (for method chaining)
     */
    public ActionChain select(By locator, int indexToSelect) {
        addAction(new SelectOption(locator, indexToSelect));
        return this;
    }

    /**
     * Adds a SelectOption UIAction for the given option to
     * the current ActionChain.
     *
     * @param locator
     * @param displayedString
     * @return the current ActionChain (for method chaining)
     */
    public ActionChain select(By locator, String displayedString) {
        addAction(new SelectOption(locator, displayedString));
        return this;
    }

    /**
     * Adds a PageAction to the current ActionChain.
     *
     * @param action
     * @param parameters
     * @return the current ActionChain (for method chaining)
     */
    public ActionChain pageAction(String action, Object... parameters) {
        addAction(new PageAction(action, parameters));
        return this;
    }

    /**
     * Adds an AssertPageAction to the current ActionChain.
     *
     * @param action
     * @param parameters
     * @return the current ActionChain (for method chaining)
     */
    public ActionChain assertPageAction(String action, Object... parameters) {
        addAction(new AssertPageAction(new PageAction(action, parameters)));
        return this;
    }

    /**
     * Adds a NewPage UIAction for the given option to the
     * current ActionChain.
     *
     * @param url
     * @return the current ActionChain (for method chaining)
     */
    public ActionChain gotoUrl(String url) {
        addAction(new NewPage(url));
        return this;
    }

    /**
     * Starts executing the action chain on the page that
     * was passed to the constructor.
     *
     * The current page is redetermined before invoking every
     * UIAction after the first one.
     *
     * After execution, the ActionChain is reset to start from
     * the last page and all the UIActions that were added are
     * removed.
     *
     * @return the last page that the AUT is in after executing all actions.
     */
    public BasePage perform() {
        return performOnPage(currentPage);
    }

    public BasePage performOnPage(BasePage page) {
        currentPage = page.executeActionChain(this);
        actions = new LinkedList<>();
        return currentPage;
    }

    protected ActionChain addAction(UIAction action) {
        actions.add(action);
        return this;
    }
}
