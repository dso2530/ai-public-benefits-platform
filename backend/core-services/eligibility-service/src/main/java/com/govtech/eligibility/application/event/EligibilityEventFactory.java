package com.govtech.eligibility.application.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.events.common.BaseEvent;
import com.govtech.events.eligibility.EligibilityCheckedEvent;
import com.govtech.events.eligibility.EligibilityResult;
import com.govtech.platform.messaging.event.EventContext;

@Component
public class EligibilityEventFactory {

    private static final String PRODUCER = "eligibility-service";

    public EligibilityCheckedEvent buildEligibilityChecked(
            String subject,
            List<Eligibility> eligibilities,
            EventContext eventContext) {

        String eventId = UUID.randomUUID().toString();

        BaseEvent metadata = BaseEvent.newBuilder()
                .setEventId(eventId)
                .setOccurredAt(
                        Instant.now().toString())
                .setCorrelationId(
                        eventContext.correlationId())
                .setCausationId(
                        eventContext.causationId())
                .setProducer(PRODUCER)
                .setSubject(subject)
                .build();

        return EligibilityCheckedEvent.newBuilder()
                .setMetadata(metadata)
                .setEligibilities(
                        eligibilities.stream()
                                .map(this::toEventResult)
                                .toList())
                .build();
    }

    private EligibilityResult toEventResult(
            Eligibility eligibility) {

        return EligibilityResult.newBuilder()
                .setAidCode(
                        eligibility.aidCode())
                .setAidName(
                        eligibility.aidName())
                .setEligible(
                        eligibility.status().isEligible())
                .setReason(
                        eligibility.reason())
                .setEstimatedAmount(
                        eligibility.estimatedAmount() != null
                                ? eligibility.estimatedAmount().toString()
                                : null)
                .build();
    }
}