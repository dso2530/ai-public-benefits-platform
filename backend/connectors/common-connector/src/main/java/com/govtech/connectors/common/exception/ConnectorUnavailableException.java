package com.govtech.connectors.common.exception;

public class ConnectorUnavailableException extends ConnectorException {

  public ConnectorUnavailableException(String message) {
    super(message);
  }

  public ConnectorUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
