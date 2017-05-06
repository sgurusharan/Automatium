package com.automatium.exception;

import org.openqa.selenium.WebDriver;

/**
 * Created by Gurusharan on 26-11-2016.
 */
public class UnknownPageException extends AutomatiumException {
    public UnknownPageException(WebDriver driver) {
        super(String.format("The test has arrived at an unknown page with title '%s' and URL '%s'", driver.getTitle(), driver.getCurrentUrl()));
    }
}
