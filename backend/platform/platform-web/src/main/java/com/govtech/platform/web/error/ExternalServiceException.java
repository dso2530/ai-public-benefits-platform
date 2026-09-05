package com.govtech.platform.web.error;

public class ExternalServiceException extends ApplicationException {

    public ExternalServiceException(String message) {
        super(
                ErrorCode.EXTERNAL_SERVICE_ERROR,
                message);
    }

    public ExternalServiceException(
            String message,
            Throwable cause) {

        super(
                ErrorCode.EXTERNAL_SERVICE_ERROR,
                message,
                cause);
    }
}
