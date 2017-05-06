package com.automatium.exception;

import com.automatium.page.BasePage;

/**
 * Created by Gurusharan on 08-04-2017.
 */
public class LocatorNotFoundException extends AutomatiumException {

    public LocatorNotFoundException(String locatorKey, BasePage page) {
        super(String.format(
                "The given locator '%s' cannot be found in page class '%s'",
                locatorKey,
                page.getClass().getSimpleName()
        ));
    }
}
