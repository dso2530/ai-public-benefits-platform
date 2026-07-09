package com.govtech.eligibility.infrastructure.persistence.exception;

public class EligibilityPersistenceException extends RuntimeException {

    public EligibilityPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}