package com.automatium.exception;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * Created by Gurusharan on 20-11-2016.
 */
public class TestPropertiesNotFoundException extends AutomatiumException {

    public TestPropertiesNotFoundException(Description testDescription, RunNotifier notifier) {
        super(String.format("No TestProperties defined for test %s in class %s", testDescription.getMethodName(), testDescription.getTestClass().getName()));
        notifier.fireTestFailure(new Failure(testDescription, this));
    }
}
