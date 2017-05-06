package com.automatium.exception;

/**
 * Created by Gurusharan on 08-04-2017.
 */
public abstract class AutomatiumException extends RuntimeException {
    public AutomatiumException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
