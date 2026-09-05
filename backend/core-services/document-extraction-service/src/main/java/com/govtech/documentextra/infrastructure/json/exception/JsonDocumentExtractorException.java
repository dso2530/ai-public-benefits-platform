package com.govtech.documentextra.infrastructure.json.exception;

public class JsonDocumentExtractorException
        extends RuntimeException {

    public JsonDocumentExtractorException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}