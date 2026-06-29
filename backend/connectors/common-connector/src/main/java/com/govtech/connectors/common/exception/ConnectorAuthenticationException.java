package com.govtech.connectors.common.exception;

public class ConnectorAuthenticationException extends ConnectorException {

    public ConnectorAuthenticationException(String message) {
        super(message);
    }

    public ConnectorAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}