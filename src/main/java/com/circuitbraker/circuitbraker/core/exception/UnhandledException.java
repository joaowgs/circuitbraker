package com.circuitbraker.circuitbraker.core.exception;

public class UnhandledException extends RuntimeException {

    public UnhandledException(Throwable throwable) {
        super(throwable);
    }
}