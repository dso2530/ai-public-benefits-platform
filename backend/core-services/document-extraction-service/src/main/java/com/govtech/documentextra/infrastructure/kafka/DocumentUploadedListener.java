package com.govtech.documentextra.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.documentextra.application.command.DocumentExtractionCommand;
import com.govtech.documentextra.application.usecase.DocumentExtractionUsecase;
import com.govtech.events.document.DocumentUploadedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.idempotency.IdempotencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentUploadedListener {

        private final DocumentExtractionUsecase documentExtractionUsecase;

        private final IdempotencyService idempotencyService;

        @Value("${messaging.kafka.group-id}")
        private String consumerGroup;

        @KafkaListener(topics = "document.uploaded", groupId = "${messaging.kafka.group-id}")
        public void consume(
                        DocumentUploadedEvent event) {

                var base = event.getBaseEvent();
                var baseEvent = base.getBase();

                String eventId = baseEvent.getEventId();
                String correlationId = baseEvent.getCorrelationId();

                Long documentId = base.getDocumentId();

                log.info(
                                "Received document.uploaded documentId={} eventId={} correlationId={}",
                                documentId,
                                eventId,
                                correlationId);

                /*
                 * ---------------------------------------------------------
                 * 1. Idempotence
                 * ---------------------------------------------------------
                 */
                boolean acquired = idempotencyService.tryAcquire(
                                consumerGroup,
                                eventId);

                if (!acquired) {

                        log.info(
                                        "Document extraction event already processed or being processed " +
                                                        "documentId={} eventId={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        consumerGroup);

                        return;
                }

                try {

                        /*
                         * ---------------------------------------------------------
                         * 2. EventContext
                         * ---------------------------------------------------------
                         *
                         * L'événement produit par document-extraction-service
                         * conservera la chaîne de causalité.
                         */
                        EventContext eventContext = EventContext.builder()
                                        .correlationId(correlationId)
                                        .causationId(eventId)
                                        .build();

                        /*
                         * ---------------------------------------------------------
                         * 3. Event -> Command
                         * ---------------------------------------------------------
                         */
                        DocumentExtractionCommand command = DocumentExtractionCommand.builder()
                                        .subject(base.getBase().getSubject())
                                        .documentId(base.getDocumentId())
                                        .bucket(base.getBucket())
                                        .objectKey(base.getObjectKey())
                                        .contentType(base.getContentType())
                                        .fileName(base.getFileName())
                                        .fileSize(base.getFileSize())
                                        .sha256(base.getSha256())
                                        .documentType(com.govtech.shared.model.DocumentType
                                                        .valueOf(base.getDocumentType().name()))
                                        .origin(com.govtech.shared.model.DocumentOrigin
                                                        .valueOf(base.getOrigin().name()))
                                        .source(base.getSource())
                                        .territoryCode(event.getTerritoryCode())
                                        .model(base.getModel())
                                        .build();

                        /*
                         * ---------------------------------------------------------
                         * 4. Traitement métier
                         * ---------------------------------------------------------
                         */
                        documentExtractionUsecase.process(
                                        command,
                                        eventContext);

                        /*
                         * ---------------------------------------------------------
                         * 5. Succès
                         * ---------------------------------------------------------
                         */
                        idempotencyService.markProcessed(
                                        consumerGroup,
                                        eventId);

                        log.info(
                                        "Document extraction event processed successfully " +
                                                        "documentId={} eventId={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        consumerGroup);

                } catch (Exception e) {

                        /*
                         * ---------------------------------------------------------
                         * 6. Échec
                         * ---------------------------------------------------------
                         *
                         * On libère la clé pour permettre le retry Kafka.
                         */
                        idempotencyService.release(
                                        consumerGroup,
                                        eventId);

                        log.error(
                                        "Document extraction event processing failed " +
                                                        "documentId={} eventId={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        consumerGroup,
                                        e);

                        throw e;
                }
        }
}