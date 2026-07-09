package com.govtech.eligibility.infrastructure.kafka.exception;

public class EligibilityPublisherException extends RuntimeException {

    public EligibilityPublisherException(String message, Throwable cause) {
        super(message, cause);
    }
}