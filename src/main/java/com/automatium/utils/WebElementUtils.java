package com.automatium.utils;

import org.openqa.selenium.WebElement;

/**
 * Created by Gurusharan on 26-11-2016.
 */
public class WebElementUtils {
    public static String webElementAsString(WebElement element) {
        String tag = element.getTagName();

        String id = element.getAttribute("id");
        id = (id == null) ? "" : id;

        String name = element.getAttribute("name");
        name = (name == null) ? "" : name;

        return String.format("<%s id='%s' name='%s'>", tag, id, name);
    }

    public static String mockOptionTagWithDisplayString(String displayString) {
        return String.format("<option ...>%s</option>", displayString);
    }

    public static String mockOptionTagWithValue(String value) {
        return String.format("<option value='%s'>...</option>", value);
    }

    public static String mockOptionTagWithValueAndString(String value, String displayString) {
        return mockOptionTagWithDisplayString(value).replaceAll("\\.\\.\\.", displayString);
    }
}
