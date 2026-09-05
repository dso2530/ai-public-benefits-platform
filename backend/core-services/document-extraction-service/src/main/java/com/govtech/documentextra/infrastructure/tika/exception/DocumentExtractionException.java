package com.govtech.documentextra.infrastructure.tika.exception;

public class DocumentExtractionException
        extends RuntimeException {

    public DocumentExtractionException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}