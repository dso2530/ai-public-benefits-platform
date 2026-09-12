package com.govtech.extraction.application.event;

import java.time.Instant;
import java.util.UUID;

import com.govtech.events.common.BaseEvent;
import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.events.common.DocumentOrigin;
import com.govtech.extraction.domain.model.Document;
import com.govtech.platform.messaging.event.EventContext;

public abstract class AbstractDocumentEventMapper<T>
        implements DocumentEventMapper<T> {

    private static final String PRODUCER = "llm-extraction-service";

    protected DocumentBaseEvent buildMetadata(
            Document document,
            String model,
            EventContext eventContext) {

        String eventId = UUID.randomUUID().toString();

        BaseEvent baseEvent = BaseEvent.newBuilder()
                .setEventId(eventId)
                .setOccurredAt(
                        Instant.now().toString())
                .setCorrelationId(
                        eventContext.correlationId())
                .setCausationId(
                        eventContext.causationId())
                .setProducer(
                        PRODUCER)
                .setSubject(
                        document.subject())
                .build();

        return DocumentBaseEvent.newBuilder()
                .setBase(baseEvent)

                .setApplicationId(
                        document.applicationId() != null
                                ? document.applicationId().toString()
                                : null)

                .setDocumentId(
                        document.documentId())

                .setBucket(
                        document.bucket())

                .setObjectKey(
                        document.objectKey())

                .setContentType(
                        document.contentType())

                .setDocumentType(
                        com.govtech.events.common.DocumentType.valueOf(
                                document.type().name()))

                .setModel(
                        model)

                .setOrigin(
                        DocumentOrigin.valueOf(
                                document.origin().name()))

                .build();
    }
}