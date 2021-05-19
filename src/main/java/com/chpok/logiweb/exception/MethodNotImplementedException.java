package com.chpok.logiweb.exception;

public class MethodNotImplementedException extends RuntimeException {
    public MethodNotImplementedException() {
        super();
    }

    public MethodNotImplementedException(String message) {
        super(message);
    }

    public MethodNotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotImplementedException(Throwable cause) {
        super(cause);
    }

    protected MethodNotImplementedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
