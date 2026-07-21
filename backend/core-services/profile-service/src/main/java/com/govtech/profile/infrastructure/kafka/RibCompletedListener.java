package com.govtech.profile.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.bank.RibExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.BankAccountMapper;
import com.govtech.profile.application.usecase.UpdateSupportingDocumentProfileUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RibCompletedListener {

    private final UpdateSupportingDocumentProfileUseCase updateSupportingDocumentProfileUseCase;

    @KafkaListener(topics = "document.rib.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(RibExtractionCompletedEvent event) {

        log.info("Received RIB extraction {}", event.getMetadata().getDocumentId());

        UpdateProfileCommand command = BankAccountMapper.toCommand(event);

        updateSupportingDocumentProfileUseCase.updateSupportingDocumentProfile(
                event.getMetadata().getSubject(),
                command, event.getMetadata());
    }
}