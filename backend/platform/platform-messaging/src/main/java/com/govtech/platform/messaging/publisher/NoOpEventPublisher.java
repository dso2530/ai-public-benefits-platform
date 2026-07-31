package com.govtech.platform.messaging.publisher;

import java.util.concurrent.CompletableFuture;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "kafka", name = "enabled", havingValue = "false")
public class NoOpEventPublisher
        implements EventPublisher {

    @Override
    public CompletableFuture<Void> publish(
            String topic,
            String key,
            Object event) {

        return CompletableFuture.completedFuture(null);

    }

}