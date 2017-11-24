package com.automatium.action;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class PageAction implements UIAction {
    private String methodName;
    private Object[] parameters;

    /**
     * Creates a UIAction that invokes a method in the page
     * class when the action is invoked. The set of parameters
     * are passed to the page method.
     *
     * The page method to call is determined using the given action
     * by capitalizing all words except the first word:
     *
     * eg 1. 'page method 1' is interpreted as pageMethod1(...)
     * eg 2. 'This PAGE METHOD' is interpreted as ThisPAGEMETHOD(...)
     *
     * @param action
     * @param parameters
     */
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
