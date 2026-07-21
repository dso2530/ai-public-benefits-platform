package com.govtech.profile.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.energy.EnergyBillExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.EnergyBillMapper;
import com.govtech.profile.application.usecase.UpdateSupportingDocumentProfileUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnergyBillCompletedListener {

    private final UpdateSupportingDocumentProfileUseCase updateSupportingDocumentProfileUseCase;

    @KafkaListener(topics = "document.energy-bill.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(EnergyBillExtractionCompletedEvent event) {

        log.info("Received energy bill extraction {}", event.getMetadata().getDocumentId());

        UpdateProfileCommand command = EnergyBillMapper.toCommand(event);

        updateSupportingDocumentProfileUseCase.updateSupportingDocumentProfile(
                event.getMetadata().getSubject(),
                command, event.getMetadata());
    }
}