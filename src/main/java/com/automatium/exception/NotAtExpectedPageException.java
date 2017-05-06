package com.automatium.exception;

import com.automatium.page.BasePage;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * Created by Gurusharan on 26-11-2016.
 */
public class NotAtExpectedPageException extends AutomatiumException {
    public NotAtExpectedPageException(BasePage page, Class expectedPageClass) {
        super(String.format("Expected current page to be '%s' but got '%s' with title '%s' and URL '%s'",
                expectedPageClass.getSimpleName(),
                page.getClass().getName(),
                page.getTitle(),
                page.getUrl()));
    }

    protected NotAtExpectedPageException(BasePage page, Set expectedPageClasses) {

        super(String.format("Expected current page to be one of the following but got '%s' with title '%s' and URL '%s': \n\t> %s\n",
                page.getClass().getName(),
                page.getTitle(),
                page.getUrl(),
                StringUtils.join(expectedPageClasses, ",\n\t> ")));
    }
}
