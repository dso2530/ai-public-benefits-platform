package com.govtech.platform.messaging.outbox;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Builder;

@ConfigurationProperties(prefix = "messaging.outbox")
@Builder
public record OutboxProperties(

        boolean enabled,

        Duration fixedDelay,

        int batchSize,

        Duration publishingTimeout,

        Duration staleAfter

) {

    public OutboxProperties {

        if (fixedDelay == null) {
            fixedDelay = Duration.ofSeconds(5);
        }

        if (batchSize <= 0) {
            batchSize = 100;
        }

        if (publishingTimeout == null) {
            publishingTimeout = Duration.ofSeconds(30);
        }

        if (staleAfter == null) {
            staleAfter = Duration.ofMinutes(5);
        }
    }

}