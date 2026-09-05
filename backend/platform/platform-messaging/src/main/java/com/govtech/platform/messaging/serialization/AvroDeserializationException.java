package com.govtech.platform.messaging.serialization;

public class AvroDeserializationException
        extends RuntimeException {

    public AvroDeserializationException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}