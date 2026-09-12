package com.govtech.extraction.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.document.DocumentExtractionCompletedEvent;
import com.govtech.extraction.application.event.DocumentMapper;
import com.govtech.extraction.application.usecase.ExtractionUsecase;
import com.govtech.extraction.domain.model.Document;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.idempotency.IdempotencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentUserExtractionCompletedListener {

    private final ExtractionUsecase extractionUsecase;

    private final DocumentMapper documentMapper;

    private final IdempotencyService idempotencyService;

    @Value("${messaging.kafka.group-id}")
    private String consumerGroup;

    @KafkaListener(topics = "document.user.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(
            DocumentExtractionCompletedEvent event) {

        var base = event.getBaseEvent();
        var metadata = base.getBase();

        Long documentId = base.getDocumentId();

        String eventId = metadata.getEventId();
        String correlationId = metadata.getCorrelationId();

        log.info(
                "Received document.user.extraction.completed " +
                        "documentId={} eventId={} correlationId={}",
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
             * 2. Propagation du contexte événementiel
             * ---------------------------------------------------------
             *
             * Le prochain événement aura :
             *
             * correlationId = correlationId de l'événement entrant
             * causationId = eventId de l'événement entrant
             */
            EventContext eventContext = EventContext.builder()
                    .correlationId(correlationId)
                    .causationId(eventId)
                    .build();

            /*
             * ---------------------------------------------------------
             * 3. Mapping Avro -> domaine
             * ---------------------------------------------------------
             */
            Document document = documentMapper.toDomain(event);

            /*
             * ---------------------------------------------------------
             * 4. Traitement métier
             * ---------------------------------------------------------
             */
            extractionUsecase.execute(
                    document,
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
             * On libère la clé pour permettre au retry Kafka
             * de reprendre le traitement.
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

            /*
             * Important :
             * l'exception remonte à Spring Kafka pour déclencher
             * le retry / DLT.
             */
            throw e;
        }
    }
}