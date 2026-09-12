package com.govtech.apply.infrastructure.kafka;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.apply.application.usecase.ApplicationUsecase;
import com.govtech.apply.domain.model.Application;
import com.govtech.apply.domain.model.ApplicationStatus;
import com.govtech.events.eligibility.EligibilityCheckedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.idempotency.IdempotencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EligibilityCheckedListener {

        private final ApplicationUsecase applicationUsecase;
        private final IdempotencyService idempotencyService;

        @Value("${messaging.kafka.group-id}")
        private String consumerGroup;

        @KafkaListener(topics = "eligibility.checked", groupId = "${messaging.kafka.group-id}")
        public void consume(EligibilityCheckedEvent event) {

                var metadata = event.getMetadata();

                String eventId = metadata.getEventId();
                String correlationId = metadata.getCorrelationId();
                String subject = metadata.getSubject();
                String occurredAt = metadata.getOccurredAt();

                log.info(
                                "Received eligibility.checked event " +
                                                "subject={} eventId={} correlationId={}  occuredAt={}",
                                subject,
                                eventId,
                                correlationId, occurredAt);

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
                                        "Eligibility event already processed or being processed " +
                                                        "subject={} eventId={} consumerGroup={}",
                                        subject,
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
                         * 4. Traitement métier
                         * ---------------------------------------------------------
                         */
                        for (var benefit : event.getEligibilities()) {

                                if (!Boolean.TRUE.equals(benefit.getEligible())) {
                                        continue;
                                }

                                Application application = Application.builder()
                                                .applicationId(UUID.randomUUID())
                                                .subject(subject)
                                                .aidCode(benefit.getAidCode())
                                                .aidName(benefit.getAidName())
                                                .status(ApplicationStatus.GENERATED)
                                                .createdAt(Instant.parse(occurredAt))
                                                .build();

                                applicationUsecase.createFromEligibility(
                                                application,
                                                eventContext);

                        }

                        /*
                         * ---------------------------------------------------------
                         * 5. Succès
                         * ---------------------------------------------------------
                         */
                        idempotencyService.markProcessed(
                                        consumerGroup,
                                        eventId);

                        log.info(
                                        "Eligibility event processed successfully " +
                                                        "subject={} eventId={} consumerGroup={}",
                                        subject,
                                        eventId,
                                        consumerGroup);

                } catch (Exception e) {

                        /*
                         * ---------------------------------------------------------
                         * 6. Échec
                         * ---------------------------------------------------------
                         *
                         * On libère la clé afin que Kafka puisse
                         * redélivrer l'événement.
                         */
                        idempotencyService.release(
                                        consumerGroup,
                                        eventId);

                        log.error(
                                        "Eligibility event processing failed " +
                                                        "subject={} eventId={} consumerGroup={}",
                                        subject,
                                        eventId,
                                        consumerGroup,
                                        e);

                        throw e;
                }
        }
}