package com.automatium.action;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class AssertPageAction implements UIAction {
    private PageAction pageAction;

    public AssertPageAction(PageAction pageAction) {
        this.pageAction = pageAction;
    }

    public PageAction getPageAction() {
        return pageAction;
    }
}
