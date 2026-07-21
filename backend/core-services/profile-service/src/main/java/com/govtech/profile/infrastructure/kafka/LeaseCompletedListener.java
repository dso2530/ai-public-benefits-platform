package com.govtech.profile.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.lease.LeaseExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.LeaseMapper;
import com.govtech.profile.application.usecase.UpdateSupportingDocumentProfileUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeaseCompletedListener {

    private final UpdateSupportingDocumentProfileUseCase updateSupportingDocumentProfileUseCase;

    @KafkaListener(topics = "document.lease.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(LeaseExtractionCompletedEvent event) {

        log.info("Received lease extraction {}", event.getMetadata().getDocumentId());

        UpdateProfileCommand command = LeaseMapper.toCommand(event);

        updateSupportingDocumentProfileUseCase.updateSupportingDocumentProfile(
                event.getMetadata().getSubject(),
                command, event.getMetadata());
    }
}