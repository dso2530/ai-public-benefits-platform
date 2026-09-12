package com.govtech.platform.messaging.idempotency;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "messaging.idempotency")
public record IdempotencyProperties(

        boolean enabled,

        Duration ttl

) {

    public IdempotencyProperties {

        if (ttl == null) {
            ttl = Duration.ofHours(24);
        }
    }
}