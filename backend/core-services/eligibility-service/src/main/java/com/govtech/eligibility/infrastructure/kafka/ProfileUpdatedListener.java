package com.govtech.eligibility.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.eligibility.application.usecase.CheckEligibilityUsecase;
import com.govtech.events.profile.ProfileUpdatedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.idempotency.IdempotencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileUpdatedListener {

    private final CheckEligibilityUsecase checkEligibilityUsecase;

    private final IdempotencyService idempotencyService;

    @Value("${messaging.kafka.group-id}")
    private String consumerGroup;

    @KafkaListener(topics = "profile.updated", groupId = "${messaging.kafka.group-id}")
    public void consume(
            ProfileUpdatedEvent event) {

        String eventId = event.getMetadata().getEventId();

        String subject = event.getMetadata().getSubject();

        String correlationId = event.getMetadata().getCorrelationId();

        log.info(
                "Profile updated subject={} eventId={} correlationId={}",
                subject,
                eventId,
                correlationId);

        boolean acquired = idempotencyService.tryAcquire(
                consumerGroup,
                eventId);

        if (!acquired) {

            log.info(
                    "Profile update already processed " +
                            "subject={} eventId={} consumerGroup={}",
                    subject,
                    eventId,
                    consumerGroup);

            return;
        }

        try {

            EventContext eventContext = EventContext.builder()
                    .correlationId(correlationId)
                    .causationId(eventId)
                    .build();

            checkEligibilityUsecase.check(
                    subject,
                    eventContext);

            idempotencyService.markProcessed(
                    consumerGroup,
                    eventId);

        } catch (Exception e) {

            idempotencyService.release(
                    consumerGroup,
                    eventId);

            log.error(
                    "Eligibility processing failed " +
                            "subject={} eventId={} consumerGroup={}",
                    subject,
                    eventId,
                    consumerGroup,
                    e);

            throw e;
        }
    }
}