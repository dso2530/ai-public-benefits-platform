package com.govtech.application.infrastructure.kafka;

import java.time.Instant;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.application.domain.model.Application;
import com.govtech.application.domain.model.ApplicationStatus;
import com.govtech.application.service.usecase.ApplicationService;
import com.govtech.events.eligibility.EligibilityCheckedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EligibilityCheckedListener {

    private final ApplicationService applicationService;

    @KafkaListener(topics = "eligibility.checked", groupId = "${messaging.kafka.group-id}")
    public void consume(EligibilityCheckedEvent event) {

        event.getEligibilities().forEach(benefit -> {
            if (benefit.getEligible())

                applicationService.create(Application.builder()
                        .applicationId(UUID.randomUUID())
                        .subject(event.getMetadata().getSubject())
                        .aidCode(benefit.getAidCode())
                        .aidName(benefit.getAidName())
                        .status(ApplicationStatus.GENERATED)
                        .createdAt(Instant.parse(event.getMetadata().getOccurredAt()))
                        .build());
        });

    }
}