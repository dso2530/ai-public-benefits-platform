package com.govtech.profile.application.service;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.outbox.OutboxEvent;
import com.govtech.platform.messaging.outbox.OutboxEventFactory;
import com.govtech.platform.messaging.outbox.OutboxStore;
import com.govtech.profile.application.dto.DocumentCommand;
import com.govtech.profile.domain.model.Citizen;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileOutboxService {

    private static final String AGGREGATE_TYPE = "CITIZEN";

    private final OutboxEventFactory outboxEventFactory;
    private final OutboxStore outboxStore;

    public <E> void publish(
            Citizen citizen,
            EventContext eventContext,
            BiFunction<Citizen, EventContext, E> eventBuilder,
            String eventType,
            String topic) {

        E event = eventBuilder.apply(
                citizen,
                eventContext);

        save(
                citizen,
                event,
                eventType,
                topic);
    }

    
    private <E> void save(
            Citizen citizen,
            E event,
            String eventType,
            String topic) {

        OutboxEvent outboxEvent = outboxEventFactory.create(
                eventType,
                topic,
                AGGREGATE_TYPE,
                citizen.getSubject(),
                event);

        outboxStore.save(outboxEvent);
    }
}