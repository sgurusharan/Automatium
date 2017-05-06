package com.automatium.exception;

import com.automatium.page.BaseHomePage;
import com.automatium.page.BasePage;
import com.automatium.utils.GlobalUtils;

/**
 * Created by Gurusharan on 08-04-2017.
 */
public class NotAtHomePageException extends NotAtExpectedPageException {
    public NotAtHomePageException(BasePage page) {
        super(page, GlobalUtils.reflections.getSubTypesOf(BaseHomePage.class));
    }
}
