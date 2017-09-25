package com.automatium.action;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class PageAction implements UIAction {
    private String methodName;
    private Object[] parameters;

    public PageAction(String action, Object... parameters) {
        action = action.trim();
        String[] actionWords = action.split(" ");
        this.methodName = actionWords[0];
        for (int i = 1; i < actionWords.length; i++) {
            this.methodName += StringUtils.capitalize(actionWords[i]);
        }
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
