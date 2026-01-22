package com.radioheaven.radioheaven_backend.exception;

import java.io.Serial;

public class CustomException extends Exception{

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }
}
