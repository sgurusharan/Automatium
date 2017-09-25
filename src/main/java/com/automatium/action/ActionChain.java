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

    public ActionChain(BasePage currentPage) {
        this.currentPage = currentPage;
    }

    public static ActionChain start(BasePage currentPage) {
        return new ActionChain(currentPage);
    }

    public ActionChain click(By locator) {
        addAction(new Click(locator));
        return this;
    }

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

    public List<UIAction> getActions() {
        return actions;
    }

    public ActionChain type(By locator, String text) {
        addAction(new TypeText(locator, text));
        return this;
    }

    public ActionChain select(By locator, SelectOption.SelectBy selectBy, String value) {
        addAction(new SelectOption(locator, selectBy, value));
        return this;
    }

    public ActionChain select(By locator, int indexToSelect) {
        addAction(new SelectOption(locator, indexToSelect));
        return this;
    }

    public ActionChain select(By locator, String displayedString) {
        addAction(new SelectOption(locator, displayedString));
        return this;
    }

    public ActionChain pageAction(String action, Object... parameters) {
        addAction(new PageAction(action, parameters));
        return this;
    }

    public ActionChain assertPageAction(String action, Object... parameters) {
        addAction(new AssertPageAction(new PageAction(action, parameters)));
        return this;
    }

    public ActionChain gotoUrl(String url) {
        addAction(new NewPage(url));
        return this;
    }

    public BasePage perform() {
        return performOnPage(currentPage);
    }

    public BasePage performOnPage(BasePage page) {
        page.executeActionChain(this);
        currentPage = page.getCurrentPage();
        actions = new LinkedList<>();
        return currentPage;
    }

    protected ActionChain addAction(UIAction action) {
        actions.add(action);
        return this;
    }
}
