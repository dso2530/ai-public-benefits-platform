package com.govtech.platform.messaging.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "messaging.kafka")
public record KafkaProperties(

        String bootstrapServers,
        String schemaRegistryUrl
) {
}