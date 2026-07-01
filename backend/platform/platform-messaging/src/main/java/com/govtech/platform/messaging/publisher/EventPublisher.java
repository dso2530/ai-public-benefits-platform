package com.govtech.platform.messaging.publisher;

public interface EventPublisher {

    void publish(String topic, String key, Object event);

}