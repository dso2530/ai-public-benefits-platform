package com.govtech.profile.application.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.events.common.BaseEvent;
import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.events.common.DocumentOrigin;
import com.govtech.events.common.DocumentType;
import com.govtech.events.profile.ProfileUpdatedEvent;
import com.govtech.events.supportingdocument.SupportingDocumentUpdatedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.profile.application.dto.DocumentCommand;
import com.govtech.profile.domain.model.Citizen;

@Component
public class ProfileEventFactory {

    private static final String PRODUCER = "profile-service";

    public ProfileUpdatedEvent buildProfileUpdated(
            Citizen citizen,
            EventContext eventContext) {

        String eventId = UUID.randomUUID().toString();

        BaseEvent metadata = BaseEvent.newBuilder()
                .setEventId(eventId)
                .setOccurredAt(Instant.now().toString())
                .setCorrelationId(eventContext.correlationId())
                .setCausationId(eventContext.causationId())
                .setProducer(PRODUCER)
                .setSubject(citizen.getSubject())
                .build();

        return ProfileUpdatedEvent.newBuilder()
                .setMetadata(metadata)
                // autres champs éventuels
                .build();
    }

    public SupportingDocumentUpdatedEvent buildSupportingDocumentUpdated(
            Citizen citizen,
            DocumentCommand documentCommand,
            EventContext eventContext) {

        String eventId = UUID.randomUUID().toString();

        BaseEvent base = BaseEvent.newBuilder()
                .setEventId(eventId)
                .setOccurredAt(Instant.now().toString())
                .setCorrelationId(eventContext.correlationId())
                .setCausationId(eventContext.causationId())
                .setProducer(PRODUCER)
                .setSubject(citizen.getSubject())
                .build();
        DocumentBaseEvent metadata = DocumentBaseEvent.newBuilder()
                .setBase(base)
                .setDocumentId(documentCommand.documentId())
                .setBucket(documentCommand.bucket())
                .setObjectKey(documentCommand.objectKey())
                .setContentType(documentCommand.contentType())
                .setFileName(documentCommand.fileName())
                .setFileSize(documentCommand.fileSize())
                .setSha256(documentCommand.sha256())
                .setDocumentType(DocumentType.valueOf(documentCommand.documentType().name()))
                .setOrigin(DocumentOrigin.valueOf(documentCommand.origin().name()))
                .build();

        return SupportingDocumentUpdatedEvent.newBuilder()
                .setMetadata(metadata)
                // autres champs éventuels
                .build();
    }
}