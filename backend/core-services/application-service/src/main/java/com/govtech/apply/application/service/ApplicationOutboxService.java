package com.govtech.apply.application.service;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.govtech.apply.domain.model.Application;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.outbox.OutboxEvent;
import com.govtech.platform.messaging.outbox.OutboxEventFactory;
import com.govtech.platform.messaging.outbox.OutboxStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationOutboxService {

    private static final String AGGREGATE_TYPE = "APPLICATION";

    private final OutboxEventFactory outboxEventFactory;
    private final OutboxStore outboxStore;

    public <E> void publish(
            Application application,
            Function<Application, E> eventBuilder,
            String eventType,
            String topic) {

        E event = eventBuilder.apply(application);

        save(
                application,
                event,
                eventType,
                topic);
    }

    public <E> void publish(
            Application application,
            EventContext eventContext,
            BiFunction<Application, EventContext, E> eventBuilder,
            String eventType,
            String topic) {

        E event = eventBuilder.apply(
                application,
                eventContext);

        save(
                application,
                event,
                eventType,
                topic);
    }

    private <E> void save(
            Application application,
            E event,
            String eventType,
            String topic) {

        OutboxEvent outboxEvent = outboxEventFactory.create(
                eventType,
                topic,
                AGGREGATE_TYPE,
                application.applicationId().toString(),
                event);

        outboxStore.save(outboxEvent);
    }
}