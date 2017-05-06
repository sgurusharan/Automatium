package com.automatium.exception;

/**
 * Created by Gurusharan on 06-12-2016.
 */
public class UnknownTargetException extends AutomatiumException {
    public UnknownTargetException(String givenTarget) {
        super("Automatium cannot identify given test target '" + givenTarget + "'");
    }
}
