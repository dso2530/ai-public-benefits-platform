package com.govtech.documentextra.application.service;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.govtech.documentextra.application.command.DocumentExtractionCommand;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.outbox.OutboxEvent;
import com.govtech.platform.messaging.outbox.OutboxEventFactory;
import com.govtech.platform.messaging.outbox.OutboxStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentExtractionOutboxService {

    private static final String AGGREGATE_TYPE = "DOCUMENT";

    private final OutboxEventFactory outboxEventFactory;
    private final OutboxStore outboxStore;

    public <E> void publish(
            DocumentExtractionCommand command,
            Function<DocumentExtractionCommand, E> eventBuilder,
            String eventType,
            String topic) {

        E event = eventBuilder.apply(command);

        save(
                command,
                event,
                eventType,
                topic);
    }

    public <E> void publish(
            DocumentExtractionCommand command,
            EventContext context,
            BiFunction<DocumentExtractionCommand, EventContext, E> eventBuilder,
            String eventType,
            String topic) {

        E event = eventBuilder.apply(
                command,
                context);

        save(
                command,
                event,
                eventType,
                topic);
    }

    private <E> void save(
            DocumentExtractionCommand command,
            E event,
            String eventType,
            String topic) {

        OutboxEvent outboxEvent = outboxEventFactory.create(
                eventType,
                topic,
                AGGREGATE_TYPE,
                command.documentId().toString(),
                event);

        outboxStore.save(outboxEvent);
    }
}