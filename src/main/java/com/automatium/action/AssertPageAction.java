package com.automatium.action;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class AssertPageAction implements UIAction {
    private PageAction pageAction;

    /**
     * Creates a UIAction that calls a boolean page method and asserts if the
     * return value is true when invoked. The page method is figured out using
     * the same way as in PageAction.
     *
     * @param pageAction
     */
    public AssertPageAction(PageAction pageAction) {
        this.pageAction = pageAction;
    }

    public PageAction getPageAction() {
        return pageAction;
    }
}
