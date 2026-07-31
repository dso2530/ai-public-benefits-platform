package com.govtech.security.domain.exception;

public class DocumentScanException extends RuntimeException {

    public DocumentScanException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

}