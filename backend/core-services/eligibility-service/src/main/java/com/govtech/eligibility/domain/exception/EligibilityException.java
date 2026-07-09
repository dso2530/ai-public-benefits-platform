package com.govtech.eligibility.domain.exception;

public class EligibilityException extends RuntimeException {

    public EligibilityException(String message) {
        super(message);
    }

    public EligibilityException(String message, Throwable cause) {
        super(message, cause);
    }
}