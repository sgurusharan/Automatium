package com.automatium.exception;

import com.automatium.page.TestEntryPage;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class InvalidFirstPageException extends AutomatiumException {
    public InvalidFirstPageException(Class<TestEntryPage> firstPageClass) {
        super("The given page class " + firstPageClass.getName() + " is not a valid entry point for test - it has no public default constructor.");
    }
}
