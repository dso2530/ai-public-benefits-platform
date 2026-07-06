package com.govtech.connectors.common.exception;

public class ConnectorUnauthorizedException extends ConnectorException {

  public ConnectorUnauthorizedException(String message) {
    super(message);
  }

  public ConnectorUnauthorizedException(String message, Throwable cause) {
    super(message, cause);
  }
}
