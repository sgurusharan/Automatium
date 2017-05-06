package com.automatium.exception;

import com.automatium.action.PageAction;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class PageActionNotFoundException extends AutomatiumException {
    public PageActionNotFoundException(PageAction pageAction, Class pageClass) {
        super(String.format("Unable to find public method '%s' with given arguments in page class '%s'", pageAction.getMethodName(), pageClass.getName()));
    }
}
