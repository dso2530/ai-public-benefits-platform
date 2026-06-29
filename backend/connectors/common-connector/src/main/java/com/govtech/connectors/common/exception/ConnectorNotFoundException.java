package com.govtech.connectors.common.exception;

public class ConnectorNotFoundException extends ConnectorException {

    public ConnectorNotFoundException(String message) {
        super(message);
    }

    public ConnectorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}