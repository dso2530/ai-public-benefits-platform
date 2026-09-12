package com.govtech.eligibility.application.service;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.outbox.OutboxEvent;
import com.govtech.platform.messaging.outbox.OutboxEventFactory;
import com.govtech.platform.messaging.outbox.OutboxStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EligibilityOutboxService {

    private static final String AGGREGATE_TYPE = "ELIGIBILITY";

    private final OutboxEventFactory outboxEventFactory;
    private final OutboxStore outboxStore;

    /**
     * Publication d'un événement sans contexte de causalité.
     */
    public <E> void publish(
            String aggregateId,
            Function<String, E> eventBuilder,
            String eventType,
            String topic) {

        E event = eventBuilder.apply(
                aggregateId);

        save(
                aggregateId,
                event,
                eventType,
                topic);
    }

    /**
     * Publication d'un événement faisant partie
     * d'une chaîne de causalité.
     */
    public <E> void publish(
            String aggregateId,
            EventContext context,
            BiFunction<String, EventContext, E> eventBuilder,
            String eventType,
            String topic) {

        E event = eventBuilder.apply(
                aggregateId,
                context);

        save(
                aggregateId,
                event,
                eventType,
                topic);
    }

    private <E> void save(
            String aggregateId,
            E event,
            String eventType,
            String topic) {

        OutboxEvent outboxEvent = outboxEventFactory.create(
                eventType,
                topic,
                AGGREGATE_TYPE,
                aggregateId,
                event);

        outboxStore.save(
                outboxEvent);
    }
}