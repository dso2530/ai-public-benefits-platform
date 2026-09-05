package com.govtech.profile.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.caf.CafCertificateExtractionCompletedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.idempotency.IdempotencyService;
import com.govtech.profile.application.dto.DocumentCommand;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.CafInformationMapper;
import com.govtech.profile.application.mapper.DocumentCommandMapper;
import com.govtech.profile.application.usecase.UpdateSupportingDocumentProfileUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CafCertificateCompletedListener {

    private final UpdateSupportingDocumentProfileUseCase updateSupportingDocumentProfileUseCase;
    private final IdempotencyService idempotencyService;

    private final DocumentCommandMapper documentCommandMapper;

    @Value("${messaging.kafka.group-id}")
    private String consumerGroup;

    @KafkaListener(topics = "document.caf-certificate.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(
            CafCertificateExtractionCompletedEvent event) {

        var metadata = event.getMetadata();
        var base = metadata.getBase();

        String eventId = base.getEventId();
        String correlationId = base.getCorrelationId();
        String subject = base.getSubject();

        log.info(
                "Received CAF certificate extraction " +
                        "documentId={} subject={} eventId={} correlationId={}",
                metadata.getDocumentId(),
                subject,
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
                    "CAF certificate extraction event already processed " +
                            "or being processed documentId={} eventId={} " +
                            "consumerGroup={}",
                    metadata.getDocumentId(),
                    eventId,
                    consumerGroup);

            return;
        }

        try {

            /*
             * ---------------------------------------------------------
             * 2. EventContext
             * ---------------------------------------------------------
             */
            EventContext eventContext = EventContext.builder()
                    .correlationId(correlationId)
                    .causationId(eventId)
                    .build();

            /*
             * ---------------------------------------------------------
             * 3. Event -> Application commands
             * ---------------------------------------------------------
             */
            UpdateProfileCommand profileCommand = CafInformationMapper.toCommand(event);

            DocumentCommand documentCommand = documentCommandMapper.toCommand(metadata);

            /*
             * ---------------------------------------------------------
             * 4. Traitement métier
             * ---------------------------------------------------------
             */
            updateSupportingDocumentProfileUseCase
                    .updateSupportingDocumentProfile(
                            subject,
                            profileCommand,
                            documentCommand,
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
                    "CAF certificate extraction event processed successfully " +
                            "documentId={} subject={} eventId={} consumerGroup={}",
                    metadata.getDocumentId(),
                    subject,
                    eventId,
                    consumerGroup);

        } catch (Exception e) {

            /*
             * ---------------------------------------------------------
             * 6. Échec
             * ---------------------------------------------------------
             *
             * On libère la clé afin que Kafka puisse redélivrer
             * l'événement.
             */
            idempotencyService.release(
                    consumerGroup,
                    eventId);

            log.error(
                    "CAF certificate extraction event processing failed " +
                            "documentId={} subject={} eventId={} consumerGroup={}",
                    metadata.getDocumentId(),
                    subject,
                    eventId,
                    consumerGroup,
                    e);

            throw e;
        }
    }
}