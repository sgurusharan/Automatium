package com.automatium.exception;

import com.automatium.action.AssertPageAction;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class InvalidAssertionException extends AutomatiumException {
    public InvalidAssertionException(AssertPageAction assertPageAction) {
        super(String.format("The method %s does not return a boolean", assertPageAction.getPageAction().getMethodName()));
    }
}
