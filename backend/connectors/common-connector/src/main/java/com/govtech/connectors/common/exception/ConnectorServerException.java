package com.govtech.connectors.common.exception;

public class ConnectorServerException extends ConnectorException {

    public ConnectorServerException(String message) {
        super(message);
    }

    public ConnectorServerException(String message, Throwable cause) {
        super(message, cause);
    }
}