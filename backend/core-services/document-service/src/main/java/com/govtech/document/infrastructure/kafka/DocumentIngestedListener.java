
package com.govtech.document.infrastructure.kafka;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.document.application.command.RegisterDocumentCommand;
import com.govtech.document.application.usecase.CreateExternalDocumentUseCase;

import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.events.document.DocumentIngestedEvent;
import com.govtech.platform.messaging.idempotency.IdempotencyService;
import com.govtech.shared.model.ConnectorType;
import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentIngestedListener {

        private final CreateExternalDocumentUseCase createExternalDocumentUseCase;

        private final IdempotencyService idempotencyService;

        @Value("${messaging.kafka.group-id}")
        private String consumerGroup;

        @KafkaListener(topics = "document.ingested", groupId = "${messaging.kafka.group-id}")
        public void consume(
                        DocumentIngestedEvent event) {

                DocumentBaseEvent base = event.getBaseEvent();

                String eventId = base.getBase()
                                .getEventId();

                Long documentId = base.getDocumentId();

                log.info(
                                "Received DocumentIngested eventId={} correlationId={} documentId={} connector={}",
                                eventId,
                                base.getBase().getCorrelationId(),
                                documentId,
                                event.getConnectorName());

                // ---------------------------------------------------------
                // Idempotence
                // ---------------------------------------------------------

                if (!idempotencyService.tryAcquire(
                                consumerGroup,
                                eventId)) {

                        log.info(
                                        "Ignoring duplicate DocumentIngested eventId={} documentId={}",
                                        eventId,
                                        documentId);

                        return;
                }

                try {

                        // -----------------------------------------------------
                        // Build command
                        // -----------------------------------------------------

                        RegisterDocumentCommand command = RegisterDocumentCommand.builder()

                                        .subject(
                                                        resolveSubject(base))

                                        .applicationId(
                                                        parseUuid(
                                                                        base.getApplicationId()))

                                        .name(
                                                        base.getFileName())

                                        .fileName(
                                                        base.getFileName())

                                        .documentType(
                                                        resolveDocumentType(base))

                                        .contentType(
                                                        base.getContentType())

                                        .fileSize(
                                                        base.getFileSize())

                                        .sha256(
                                                        base.getSha256())

                                        .uploadedAt(
                                                        Instant.parse(
                                                                        base.getBase()
                                                                                        .getOccurredAt()))

                                        .origin(
                                                        DocumentOrigin.CONNECTOR)

                                        .source(
                                                        base.getSource())

                                        .connectorName(
                                                        event.getConnectorName())

                                        .connectorType(
                                                        resolveConnectorType(
                                                                        event.getConnectorType()))

                                        .sourceUri(
                                                        event.getSourceUri())

                                        .territoryCode(
                                                        base.getTerritoryCode())

                                        .bucket(base.getBucket())

                                        .objectKey(base.getObjectKey())

                                        .metadata(
                                                        buildMetadata(base))

                                        .build();

                        // -----------------------------------------------------
                        // Business processing
                        // -----------------------------------------------------

                        createExternalDocumentUseCase.execute(command);

                        // -----------------------------------------------------
                        // Mark processed
                        // -----------------------------------------------------

                        idempotencyService.markProcessed(
                                        consumerGroup,
                                        eventId);

                        log.info(
                                        "DocumentIngested processed successfully eventId={} documentId={}",
                                        eventId,
                                        documentId);

                } catch (Exception e) {

                        /*
                         * Le traitement a échoué.
                         *
                         * On libère la clé Redis pour que le retry Kafka
                         * puisse reprendre le traitement.
                         */
                        idempotencyService.release(
                                        consumerGroup,
                                        eventId);

                        log.error(
                                        "DocumentIngested processing failed eventId={} documentId={}",
                                        eventId,
                                        documentId,
                                        e);

                        throw e;
                }
        }

        private String resolveSubject(
                        DocumentBaseEvent base) {

                String subject = base.getBase().getSubject();

                return subject != null && !subject.isBlank()
                                ? subject
                                : "SYSTEM";
        }

        private UUID parseUuid(
                        String value) {

                if (value == null || value.isBlank()) {
                        return null;
                }

                return UUID.fromString(value);
        }

        private DocumentType resolveDocumentType(
                        DocumentBaseEvent base) {

                if (base.getDocumentType() == null) {
                        return DocumentType.OTHER;
                }

                try {
                        return DocumentType.valueOf(
                                        base.getDocumentType().name());

                } catch (IllegalArgumentException e) {
                        return DocumentType.OTHER;
                }
        }

        private ConnectorType resolveConnectorType(
                        com.govtech.events.connector.ConnectorType type) {

                if (type == null) {
                        return null;
                }

                try {
                        return ConnectorType.valueOf(
                                        type.name());

                } catch (IllegalArgumentException e) {
                        return null;
                }
        }

        private Map<String, String> buildMetadata(
                        DocumentBaseEvent base) {

                return Map.of(
                                "bucket",
                                base.getBucket(),
                                "objectKey",
                                base.getObjectKey());
        }
}
