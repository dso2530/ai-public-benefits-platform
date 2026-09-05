package com.govtech.documentextra.application.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.documentextra.application.command.DocumentExtractionCommand;
import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.events.common.BaseEvent;
import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.events.common.DocumentOrigin;
import com.govtech.events.common.DocumentType;
import com.govtech.events.document.DocumentExtractionCompletedEvent;
import com.govtech.platform.messaging.event.EventContext;

@Component
public class DocumentExtractionEventFactory {

        private static final String PRODUCER = "document-extraction-service";

        /**
         * Construit l'événement DocumentExtractionCompleted.
         *
         * eventId = nouveau UUID
         * correlationId = celui de l'événement parent
         * causationId = eventId de DocumentUploaded
         */
        public DocumentExtractionCompletedEvent buildExtractionCompleted(
                        DocumentExtractionCommand command,
                        ExtractionResult result,
                        EventContext eventContext) {

                String eventId = UUID.randomUUID().toString();

                return DocumentExtractionCompletedEvent.newBuilder()
                                .setBaseEvent(
                                                buildDocumentBase(
                                                                command,
                                                                eventId,
                                                                eventContext))
                                .setExtractionType(
                                                result.extractedType())
                                .setExtractedContentType(
                                                result.contentType())
                                .setText(
                                                result.text())
                                .setProcessedAt(
                                                Instant.now().toString())
                                .setMetadata(
                                                result.metadata())
                                .setTerritoryCode(
                                                command.territoryCode())
                                .build();
        }

        /**
         * Construction du DocumentBaseEvent commun.
         */
        private DocumentBaseEvent buildDocumentBase(
                        DocumentExtractionCommand command,
                        String eventId,
                        EventContext eventContext) {

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
                                                command.subject())
                                .build();

                return DocumentBaseEvent.newBuilder()
                                .setBase(baseEvent)

                                .setDocumentId(
                                                command.documentId())

                                .setApplicationId(
                                                command.applicationId())

                                .setBucket(
                                                command.bucket())

                                .setObjectKey(
                                                command.objectKey())

                                .setContentType(
                                                command.contentType())

                                .setDocumentType(
                                                DocumentType.valueOf(command.documentType().name()))

                                .setOrigin(
                                                DocumentOrigin.valueOf(command.origin().name()))

                                .setSource(
                                                command.source())

                                .setSha256(
                                                command.sha256())

                                .setFileName(
                                                command.fileName())

                                .setFileSize(
                                                command.fileSize())

                                .setModel(
                                                command.model())

                                .build();
        }
}