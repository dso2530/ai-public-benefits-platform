package com.govtech.apply.application.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.apply.domain.model.Application;
import com.govtech.events.application.ApplicationGeneratedEvent;
import com.govtech.events.application.ApplicationSubmittedEvent;
import com.govtech.events.common.BaseEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.web.correlation.CorrelationId;

@Component
public class ApplicationEventFactory {

        private static final String PRODUCER = "application-service";

        public ApplicationSubmittedEvent buildApplicationSubmitted(
                        Application application) {

                String eventId = UUID.randomUUID().toString();

                BaseEvent metadata = BaseEvent.newBuilder()
                                .setEventId(eventId)
                                .setOccurredAt(
                                                Instant.now().toString())
                                .setCorrelationId(
                                                CorrelationId.getOrCreate())
                                .setCausationId(null)
                                .setProducer(PRODUCER)
                                .setSubject(
                                                application.subject())
                                .build();

                return ApplicationSubmittedEvent.newBuilder()
                                .setMetadata(metadata)
                                // autres champs de l'événement
                                .build();
        }

        public ApplicationGeneratedEvent buildApplicationGenerated(
                        Application application,
                        EventContext eventContext) {

                String eventId = UUID.randomUUID().toString();

                BaseEvent metadata = BaseEvent.newBuilder()
                                .setEventId(eventId)
                                .setOccurredAt(Instant.now().toString())
                                .setCorrelationId(eventContext.correlationId())
                                .setCausationId(eventContext.causationId())
                                .setProducer(PRODUCER)
                                .setSubject(application.subject())
                                .build();

                return ApplicationGeneratedEvent.newBuilder()
                                .setMetadata(metadata)
                                .setApplicationId(application.applicationId().toString())
                                .setAidCode(application.aidCode())
                                .setAidName(application.aidName())
                                .setStatus(application.status().name())
                                .build();
        }

}