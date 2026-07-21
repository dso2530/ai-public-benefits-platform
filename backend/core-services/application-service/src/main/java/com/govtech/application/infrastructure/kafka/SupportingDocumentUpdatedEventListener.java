package com.govtech.application.infrastructure.kafka;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.application.service.usecase.ApplicationService;
import com.govtech.events.supportingdocument.SupportingDocumentUpdatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SupportingDocumentUpdatedEventListener {

    private final ApplicationService applicationService;

    @KafkaListener(topics = "supporting.document.updated", groupId = "${messaging.kafka.group-id}")
    public void consume(SupportingDocumentUpdatedEvent event) {

        if (event.getMetadata().getApplicationId() == null) {
            return;
        }

        applicationService.refreshApplication(
                UUID.fromString(event.getMetadata().getApplicationId()), event.getMetadata().getSubject());
    }
}