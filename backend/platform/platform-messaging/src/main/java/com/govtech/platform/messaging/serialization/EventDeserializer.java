package com.govtech.platform.messaging.serialization;

public interface EventDeserializer {

    <T> T deserialize(
            byte[] payload,
            Class<T> targetType);
}