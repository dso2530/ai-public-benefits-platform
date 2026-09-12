package com.govtech.apply.application.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.apply.domain.model.Application;
import com.govtech.events.application.ApplicationGeneratedEvent;
import com.govtech.events.common.BaseEvent;

@Component
public class ApplicationEventMapper {

    public ApplicationGeneratedEvent toEvent(Application application) {

        return ApplicationGeneratedEvent.newBuilder()
                .setApplicationId(application.applicationId().toString())
                .setMetadata(
                        BaseEvent.newBuilder()
                                .setEventId(UUID.randomUUID().toString())
                                .setOccurredAt(Instant.now().toString())
                                .setSubject(application.subject())
                                .setCausationId(null)
                                .setCorrelationId(UUID.randomUUID().toString())
                                .setProducer("application-service")
                                .build())
                .setAidCode(application.aidCode())
                .setAidName(application.aidName())
                .setStatus(application.status().name())
                .setObjectKey(application.objectKey())
                .build();
    }
}