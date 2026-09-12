package com.govtech.profile.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.tax.TaxNoticeExtractionCompletedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.idempotency.IdempotencyService;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.TaxNoticeProfileMapper;
import com.govtech.profile.application.usecase.UpdateProfileTaxUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaxNoticeCompletedListener {

    private final UpdateProfileTaxUseCase updateProfileTaxUseCase;
    private final IdempotencyService idempotencyService;

    @Value("${messaging.kafka.group-id}")
    private String consumerGroup;

    @KafkaListener(topics = "document.tax-notice.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(
            TaxNoticeExtractionCompletedEvent event) {

        var metadata = event.getMetadata();
        var base = metadata.getBase();

        String eventId = base.getEventId();
        String subject = base.getSubject();

        log.info(
                "Received tax notice extraction " +
                        "documentId={} subject={} eventId={}",
                metadata.getDocumentId(),
                subject,
                eventId);

        if (!idempotencyService.tryAcquire(
                consumerGroup,
                eventId)) {

            log.info(
                    "Tax notice event already processed or being processed " +
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

            UpdateProfileCommand profileCommand = TaxNoticeProfileMapper.toCommand(event);

            updateProfileTaxUseCase.updateProfileTax(
                    subject,
                    profileCommand,
                    eventContext);

            idempotencyService.markProcessed(
                    consumerGroup,
                    eventId);

        } catch (Exception e) {

            idempotencyService.release(
                    consumerGroup,
                    eventId);

            log.error(
                    "Tax notice extraction processing failed " +
                            "documentId={} eventId={}",
                    metadata.getDocumentId(),
                    eventId,
                    e);

            throw e;
        }
    }
}