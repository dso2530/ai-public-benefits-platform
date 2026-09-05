package com.govtech.platform.web.error;

public class ExternalServiceUnavailableException
        extends ApplicationException {

    public ExternalServiceUnavailableException(
            String message) {

        super(
                ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
                message);
    }

    public ExternalServiceUnavailableException(
            String message,
            Throwable cause) {

        super(
                ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
                message,
                cause);
    }
}
