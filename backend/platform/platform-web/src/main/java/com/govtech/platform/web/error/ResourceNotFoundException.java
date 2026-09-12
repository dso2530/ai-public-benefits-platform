package com.govtech.platform.web.error;

public class ResourceNotFoundException
        extends ApplicationException {

    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}