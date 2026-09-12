package com.govtech.security.application.service;

import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.outbox.OutboxEvent;
import com.govtech.platform.messaging.outbox.OutboxEventFactory;
import com.govtech.platform.messaging.outbox.OutboxStore;
import com.govtech.security.domain.model.SecurityScan;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityOutboxService {

    private static final String AGGREGATE_TYPE = "DOCUMENT";

    private final OutboxEventFactory outboxEventFactory;
    private final OutboxStore outboxStore;

    /**
     * Publication d'un événement faisant partie
     * d'une chaîne de causalité.
     */
    public <E> void publish(
            SecurityScan scan,
            EventContext context,
            BiFunction<SecurityScan, EventContext, E> eventBuilder,
            String eventType,
            String topic) {

        E event = eventBuilder.apply(
                scan,
                context);

        save(
                scan,
                event,
                eventType,
                topic);
    }

    private <E> void save(
            SecurityScan scan,
            E event,
            String eventType,
            String topic) {

        OutboxEvent outboxEvent = outboxEventFactory.create(
                eventType,
                topic,
                AGGREGATE_TYPE,
                scan.getDocumentId().toString(),
                event);

        outboxStore.save(outboxEvent);
    }
}