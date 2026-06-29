package com.govtech.connectors.common.exception;

public class ConnectorAccessDeniedException extends ConnectorException {

    public ConnectorAccessDeniedException(String message) {
        super(message);
    }

    public ConnectorAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}