package com.govtech.profile.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.bank.RibExtractionCompletedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.idempotency.IdempotencyService;
import com.govtech.profile.application.dto.DocumentCommand;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.BankAccountMapper;
import com.govtech.profile.application.mapper.DocumentCommandMapper;
import com.govtech.profile.application.usecase.UpdateSupportingDocumentProfileUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RibCompletedListener {

    private final UpdateSupportingDocumentProfileUseCase updateSupportingDocumentProfileUseCase;
    private final IdempotencyService idempotencyService;
    private final DocumentCommandMapper documentCommandMapper;

    @Value("${messaging.kafka.group-id}")
    private String consumerGroup;

    @KafkaListener(topics = "document.rib.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(
            RibExtractionCompletedEvent event) {

        var metadata = event.getMetadata();
        var base = metadata.getBase();

        String eventId = base.getEventId();
        String subject = base.getSubject();

        log.info(
                "Received RIB extraction " +
                        "documentId={} subject={} eventId={}",
                metadata.getDocumentId(),
                subject,
                eventId);

        if (!idempotencyService.tryAcquire(
                consumerGroup,
                eventId)) {

            log.info(
                    "RIB event already processed or being processed " +
                            "documentId={} eventId={}",
                    metadata.getDocumentId(),
                    eventId);

            return;
        }

        try {

            EventContext eventContext = EventContext.builder()
                    .correlationId(base.getCorrelationId())
                    .causationId(eventId)
                    .build();

            UpdateProfileCommand profileCommand = BankAccountMapper.toCommand(event);

            DocumentCommand documentCommand = documentCommandMapper.toCommand(metadata);

            updateSupportingDocumentProfileUseCase
                    .updateSupportingDocumentProfile(
                            subject,
                            profileCommand,
                            documentCommand,
                            eventContext);

            idempotencyService.markProcessed(
                    consumerGroup,
                    eventId);

        } catch (Exception e) {

            idempotencyService.release(
                    consumerGroup,
                    eventId);

            log.error(
                    "RIB extraction processing failed " +
                            "documentId={} eventId={}",
                    metadata.getDocumentId(),
                    eventId,
                    e);

            throw e;
        }
    }
}