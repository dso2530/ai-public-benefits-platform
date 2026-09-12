package com.govtech.documentextra.infrastructure.archive.exception;

public class ArchiveExtractionException
        extends RuntimeException {

    public ArchiveExtractionException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}