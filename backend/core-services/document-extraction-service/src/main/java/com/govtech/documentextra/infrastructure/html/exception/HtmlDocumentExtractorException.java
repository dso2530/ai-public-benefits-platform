package com.govtech.documentextra.infrastructure.html.exception;

public class HtmlDocumentExtractorException
        extends RuntimeException {

    public HtmlDocumentExtractorException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}