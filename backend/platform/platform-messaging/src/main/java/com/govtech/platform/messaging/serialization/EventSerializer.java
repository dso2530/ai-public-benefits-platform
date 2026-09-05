package com.govtech.platform.messaging.serialization;

public interface EventSerializer {

    byte[] serialize(Object event);
}