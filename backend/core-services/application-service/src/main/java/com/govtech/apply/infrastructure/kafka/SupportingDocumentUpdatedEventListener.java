package com.govtech.apply.infrastructure.kafka;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.apply.application.usecase.ApplicationUsecase;
import com.govtech.events.supportingdocument.SupportingDocumentUpdatedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.idempotency.IdempotencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SupportingDocumentUpdatedEventListener {

    private final ApplicationUsecase applicationUsecase;
    private final IdempotencyService idempotencyService;

    @Value("${messaging.kafka.group-id}")
    private String consumerGroup;

    @KafkaListener(topics = "supporting.document.updated", groupId = "${messaging.kafka.group-id}")
    public void consume(
            SupportingDocumentUpdatedEvent event) {

        var metadata = event.getMetadata();

        var base = metadata.getBase();

        String eventId = base.getEventId();
        String correlationId = base.getCorrelationId();
        String subject = base.getSubject();
        String applicationIdValue = metadata.getApplicationId();

        log.info(
                "Received supporting.document.updated " +
                        "applicationId={} eventId={} correlationId={} subject={}",
                applicationIdValue,
                eventId,
                correlationId,
                subject);

        /*
         * ---------------------------------------------------------
         * 1. Validation
         * ---------------------------------------------------------
         */
        if (applicationIdValue == null || applicationIdValue.isBlank()) {

            log.warn(
                    "Supporting document update ignored: missing applicationId " +
                            "eventId={} subject={}",
                    eventId,
                    subject);

            return;
        }

        UUID applicationId;

        try {
            applicationId = UUID.fromString(applicationIdValue);
        } catch (IllegalArgumentException e) {

            log.error(
                    "Invalid applicationId in supporting.document.updated " +
                            "applicationId={} eventId={} subject={}",
                    applicationIdValue,
                    eventId,
                    subject,
                    e);

            throw e;
        }

        /*
         * ---------------------------------------------------------
         * 2. Idempotence
         * ---------------------------------------------------------
         */
        boolean acquired = idempotencyService.tryAcquire(
                consumerGroup,
                eventId);

        if (!acquired) {

            log.info(
                    "Supporting document update already processed or being processed applicationId={} eventId={} consumerGroup={}",
                    applicationId,
                    eventId,
                    consumerGroup);

            return;
        }

        try {

            /*
             * ---------------------------------------------------------
             * 4. Traitement métier
             * ---------------------------------------------------------
             */
            applicationUsecase.refreshApplication(
                    applicationId,
                    subject);

            /*
             * ---------------------------------------------------------
             * 5. Succès
             * ---------------------------------------------------------
             */
            idempotencyService.markProcessed(
                    consumerGroup,
                    eventId);

            log.info(
                    "Supporting document update processed successfully " +
                            "applicationId={} eventId={} consumerGroup={}",
                    applicationId,
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
                    "Supporting document update processing failed " +
                            "applicationId={} eventId={} consumerGroup={}",
                    applicationId,
                    eventId,
                    consumerGroup,
                    e);

            throw e;
        }
    }
}