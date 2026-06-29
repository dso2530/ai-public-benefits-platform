package com.govtech.connectors.common.exception;

public class ConnectorRateLimitException extends ConnectorException {

    public ConnectorRateLimitException(String message) {
        super(message);
    }

    public ConnectorRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}