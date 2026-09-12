package com.govtech.platform.messaging.outbox;

import java.time.Instant;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventFactory {

    public OutboxEvent create(
            String eventType,
            String topic,
            String aggregateType,
            String aggregateId,
            Object event) {

        return OutboxEvent.builder()
                .id(UUID.randomUUID())
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .topic(topic)
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .payload(event)
                .status(OutboxStatus.READY_TO_PUBLISH)
                .createdAt(Instant.now())
                .attempts(0)
                .build();
    }
}