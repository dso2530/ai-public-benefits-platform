package com.govtech.connectors.common.exception;

public class ConnectorRetryException extends ConnectorException {

  public ConnectorRetryException(String message) {
    super(message);
  }

  public ConnectorRetryException(String message, Throwable cause) {
    super(message, cause);
  }
}
