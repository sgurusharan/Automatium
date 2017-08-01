package com.automatium.action;

import com.automatium.page.BasePage;
import org.openqa.selenium.By;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class ActionChain {
    // Test Comment
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
