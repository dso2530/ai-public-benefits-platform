package com.govtech.documentextra.infrastructure.csv.exception;


public class CsvDocumentExtractorException
        extends RuntimeException {

    public CsvDocumentExtractorException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}