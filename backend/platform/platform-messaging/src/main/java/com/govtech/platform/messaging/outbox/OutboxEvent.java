package com.govtech.platform.messaging.outbox;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;

@Builder
public record OutboxEvent(

                UUID id,

                String eventId,

                String eventType,

                String topic,

                String aggregateType,

                String aggregateId,

                Object payload,

                OutboxStatus status,

                Instant createdAt,

                Instant publishingStartedAt,

                Instant publishedAt,

                int attempts,

                String lastError

) {
}