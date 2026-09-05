package com.govtech.knowledge.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.document.DocumentExtractionCompletedEvent;
import com.govtech.knowledge.application.usecase.DocumentIndexUseCase;
import com.govtech.platform.messaging.idempotency.IdempotencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentExternalExtractionCompletedListener {

        private final DocumentIndexUseCase documentIdexationUseCase;

        private final IdempotencyService idempotencyService;

        @Value("${messaging.kafka.group-id}")
        private String consumerGroup;

        @KafkaListener(topics = "document.external.extraction.completed", groupId = "${messaging.kafka.group-id}")
        public void consume(
                        DocumentExtractionCompletedEvent event) {

                var baseEvent = event.getBaseEvent();

                Long documentId = baseEvent.getDocumentId();

                String eventId = baseEvent
                                .getBase()
                                .getEventId();

                log.info(
                                "Received DocumentExtractionCompletedEvent " +
                                                "documentId={} eventId={} source={} documentType={} extractionType={}",
                                documentId,
                                eventId,
                                baseEvent.getSource(),
                                baseEvent.getDocumentType(),
                                event.getExtractionType());

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

                        log.debug(
                                        "Extraction content metadata " +
                                                        "documentId={} territoryCode={} extractedContentType={} textLength={}",
                                        documentId,
                                        event.getTerritoryCode(),
                                        event.getExtractedContentType(),
                                        event.getText() != null
                                                        ? event.getText().length()
                                                        : 0);

                        /*
                         * ---------------------------------------------------------
                         * 2. Traitement métier
                         * ---------------------------------------------------------
                         */
                        documentIdexationUseCase.execute(
                                        documentId,
                                        baseEvent.getSource(),
                                        event.getTerritoryCode(),
                                        baseEvent.getDocumentType().name(),
                                        event.getText());

                        /*
                         * ---------------------------------------------------------
                         * 3. Succès
                         * ---------------------------------------------------------
                         */
                        idempotencyService.markProcessed(
                                        consumerGroup,
                                        eventId);

                        log.info(
                                        "Document indexed successfully " +
                                                        "documentId={} eventId={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        consumerGroup);

                } catch (Exception e) {

                        /*
                         * ---------------------------------------------------------
                         * 4. Échec
                         * ---------------------------------------------------------
                         *
                         * On libère l'idempotency key afin que Kafka puisse
                         * rejouer l'événement.
                         */
                        idempotencyService.release(
                                        consumerGroup,
                                        eventId);

                        log.error(
                                        "Document indexing failed " +
                                                        "documentId={} eventId={} source={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        baseEvent.getSource(),
                                        consumerGroup,
                                        e);

                        /*
                         * L'exception doit remonter à Spring Kafka
                         * pour déclencher retry / DLT.
                         */
                        throw e;
                }
        }
}