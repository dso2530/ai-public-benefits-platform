package com.govtech.security.domain.exception;

public class AntivirusScanException extends RuntimeException {

    public AntivirusScanException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

}
