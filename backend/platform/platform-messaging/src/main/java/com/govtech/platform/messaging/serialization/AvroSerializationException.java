package com.govtech.platform.messaging.serialization;

public class AvroSerializationException
        extends RuntimeException {

    public AvroSerializationException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}