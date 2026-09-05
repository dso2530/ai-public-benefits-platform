package com.govtech.document.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.document.application.command.UpdateDocumentSecurityCommand;
import com.govtech.document.application.usecase.UpdateExternalDocumentSecurityStatusUseCase;
import com.govtech.document.application.usecase.UpdateUploadDocumentSecurityStatusUseCase;
import com.govtech.events.common.DocumentOrigin;
import com.govtech.events.security.DocumentScanCompletedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.idempotency.IdempotencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentScanCompletedListener {

        private final UpdateUploadDocumentSecurityStatusUseCase uploadUseCase;

        private final UpdateExternalDocumentSecurityStatusUseCase externalUseCase;

        private final IdempotencyService idempotencyService;

        @Value("${messaging.kafka.group-id}")
        private String consumerGroup;

        @KafkaListener(topics = "document.scan.completed", groupId = "${messaging.kafka.group-id}")
        public void consume(DocumentScanCompletedEvent event) {

                var baseEvent = event.getBaseEvent();
                var eventBase = baseEvent.getBase();

                String eventId = eventBase.getEventId();

                String correlationId = eventBase.getCorrelationId();

                Long documentId = baseEvent.getDocumentId();

                DocumentOrigin origin = baseEvent.getOrigin();

                log.info(
                                "Document security scan completed documentId={} status={} origin={} eventId={} correlationId={}",
                                documentId,
                                event.getSecurityStatus(),
                                origin,
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
                                        "Document scan event already processed or being processed documentId={} eventId={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        consumerGroup);

                        return;
                }

                try {

                        /*
                         * -----------------------------------------------------
                         * 2. EventContext
                         * -----------------------------------------------------
                         *
                         * correlationId :
                         * conservé depuis l'événement entrant.
                         *
                         * causationId :
                         * devient l'eventId de DocumentScanCompleted.
                         */
                        EventContext eventContext = EventContext.builder()
                                        .correlationId(correlationId)
                                        .causationId(eventId)
                                        .build();

                        /*
                         * -----------------------------------------------------
                         * 3. Command
                         * -----------------------------------------------------
                         */
                        UpdateDocumentSecurityCommand command = UpdateDocumentSecurityCommand.builder()
                                        .documentId(documentId)
                                        .securityStatus(event.getSecurityStatus())
                                        .scanEngine(event.getScanEngine())
                                        .scannedAt(event.getScannedAt())
                                        .sha256(baseEvent.getSha256())
                                        .detectedContentType(event.getDetectedContentType()).build();

                        /*
                         * -----------------------------------------------------
                         * 4. Traitement métier
                         * -----------------------------------------------------
                         */
                        switch (origin) {

                                case USER_UPLOAD ->

                                        uploadUseCase.execute(
                                                        command,
                                                        eventContext);

                                case CONNECTOR ->

                                        externalUseCase.execute(
                                                        command,
                                                        eventContext);

                                case SYSTEM_GENERATED ->

                                        handleSystemGeneratedDocument(
                                                        documentId,
                                                        eventId);

                                default -> throw new IllegalStateException(
                                                "Unsupported document origin "
                                                                + origin);
                        }

                        /*
                         * -----------------------------------------------------
                         * 5. Succès
                         * -----------------------------------------------------
                         *
                         * L'événement est marqué comme traité uniquement
                         * après succès du traitement métier.
                         */
                        idempotencyService.markProcessed(
                                        consumerGroup,
                                        eventId);

                        log.info(
                                        "Document scan event processed successfully documentId={} eventId={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        consumerGroup);

                } catch (Exception e) {

                        /*
                         * -----------------------------------------------------
                         * 6. Échec
                         * -----------------------------------------------------
                         *
                         * On libère la clé d'idempotence pour permettre
                         * un nouveau traitement lors du retry Kafka.
                         */
                        idempotencyService.release(
                                        consumerGroup,
                                        eventId);

                        log.error(
                                        "Document scan event processing failed documentId={} eventId={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        consumerGroup,
                                        e);

                        throw e;
                }
        }

        private void handleSystemGeneratedDocument(
                        Long documentId,
                        String eventId) {

                log.warn(
                                "System generated document security handling not implemented documentId={} eventId={}",
                                documentId,
                                eventId);

                /*
                 * Important :
                 *
                 * Si tu considères SYSTEM_GENERATED comme un cas non traité,
                 * il vaut mieux lever une exception.
                 *
                 * Sinon, l'événement sera marqué comme PROCESSED alors qu'aucun
                 * traitement métier n'a réellement été effectué.
                 */
                throw new UnsupportedOperationException(
                                "SYSTEM_GENERATED document security handling is not implemented");
        }
}