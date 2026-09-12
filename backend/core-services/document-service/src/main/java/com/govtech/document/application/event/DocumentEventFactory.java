package com.govtech.document.application.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.document.domain.model.Document;
import com.govtech.events.common.BaseEvent;
import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.events.common.DocumentOrigin;
import com.govtech.events.common.DocumentType;
import com.govtech.events.document.DocumentUploadedEvent;
import com.govtech.events.security.DocumentScanRequestedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.web.correlation.CorrelationId;

@Component
public class DocumentEventFactory {

        private static final String PRODUCER = "document-service";

        /**
         * Premier événement de la chaîne.
         *
         * eventId = nouveau UUID
         * correlationId = nouvelle chaîne
         * causationId = null
         */
        public DocumentScanRequestedEvent buildScanRequested(
                        Document document) {

                String eventId = UUID.randomUUID().toString();

                String correlationId = CorrelationId.getOrCreate();

                return DocumentScanRequestedEvent.newBuilder()
                                .setBaseEvent(
                                                buildDocumentBase(
                                                                document,
                                                                eventId,
                                                                correlationId,
                                                                null))
                                .setUploadedAt(
                                                document.getUploadedAt().toString())
                                .build();
        }

        /**
         * Événement suivant dans la chaîne.
         *
         * eventId = nouveau UUID
         * correlationId = celui de l'événement entrant
         * causationId = eventId de l'événement entrant
         */
        public DocumentUploadedEvent buildUploaded(
                        Document document,
                        EventContext context) {

                String eventId = UUID.randomUUID().toString();

                return DocumentUploadedEvent.newBuilder()
                                .setBaseEvent(
                                                buildDocumentBase(
                                                                document,
                                                                eventId,
                                                                context.correlationId(),
                                                                context.causationId()))
                                .setUploadedAt(
                                                document.getUploadedAt().toString())
                                .build();
        }

        private DocumentBaseEvent buildDocumentBase(
                        Document document,
                        String eventId,
                        String correlationId,
                        String causationId) {

                BaseEvent baseEvent = BaseEvent.newBuilder()
                                .setEventId(eventId)
                                .setOccurredAt(
                                                Instant.now().toString())
                                .setCorrelationId(
                                                correlationId)
                                .setCausationId(
                                                causationId)
                                .setProducer(
                                                PRODUCER)
                                .setSubject(
                                                document.getSubject())
                                .build();

                return DocumentBaseEvent.newBuilder()
                                .setBase(baseEvent)

                                .setDocumentId(
                                                document.getId())

                                .setApplicationId(
                                                document.getApplicationId() != null
                                                                ? document.getApplicationId().toString()
                                                                : null)

                                .setBucket(
                                                document.getBucket())

                                .setObjectKey(
                                                document.getObjectKey())

                                .setContentType(
                                                document.getContentType())

                                .setDocumentType(
                                                toAvroDocumentType(
                                                                document.getDocumentType()))

                                .setOrigin(
                                                toAvroDocumentOrigin(
                                                                document.getOrigin()))

                                .setSource(
                                                document.getSource())

                                .setSha256(
                                                document.getSha256())

                                .setFileName(
                                                document.getFileName())

                                .setFileSize(
                                                document.getFileSize())

                                .setModel(
                                                document.getModel())

                                .build();
        }

        private DocumentType toAvroDocumentType(
                        com.govtech.shared.model.DocumentType type) {

                return type == null
                                ? null
                                : DocumentType.valueOf(type.name());
        }

        private DocumentOrigin toAvroDocumentOrigin(
                        com.govtech.shared.model.DocumentOrigin origin) {

                return origin == null
                                ? null
                                : DocumentOrigin.valueOf(origin.name());
        }
}