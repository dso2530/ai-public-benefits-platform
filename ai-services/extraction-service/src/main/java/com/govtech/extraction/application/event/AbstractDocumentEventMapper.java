package com.govtech.extraction.application.event;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.extraction.domain.model.Document;

public abstract class AbstractDocumentEventMapper<T> implements DocumentEventMapper<T> {

    protected DocumentBaseEvent buildMetadata(Document document, String model) {
        return DocumentBaseEvent.newBuilder()
                .setApplicationId(null != document.applicationId() ? document.applicationId().toString() : null)
                .setEventId(UUID.randomUUID().toString())
                .setOccurredAt(OffsetDateTime.now().toString())
                .setDocumentId(document.documentId())
                .setSubject(document.subject())
                .setBucket(document.bucket())
                .setObjectKey(document.objectKey())
                .setContentType(document.contentType())
                .setDocumentType(
                        com.govtech.events.common.DocumentType.valueOf(document.type().name()))
                .setModel(model)
                .build();
    }
}