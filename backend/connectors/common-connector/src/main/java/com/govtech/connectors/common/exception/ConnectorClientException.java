package com.govtech.connectors.common.exception;

public class ConnectorClientException extends ConnectorException {

  public ConnectorClientException(String message) {
    super(message);
  }

  public ConnectorClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
