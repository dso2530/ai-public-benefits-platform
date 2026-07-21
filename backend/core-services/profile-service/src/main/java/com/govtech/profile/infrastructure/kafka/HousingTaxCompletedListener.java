package com.govtech.profile.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.housingtax.HousingTaxExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.HousingTaxMapper;
import com.govtech.profile.application.usecase.UpdateSupportingDocumentProfileUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HousingTaxCompletedListener {

    private final UpdateSupportingDocumentProfileUseCase updateSupportingDocumentProfileUseCase;

    @KafkaListener(topics = "document.housing-tax.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(HousingTaxExtractionCompletedEvent event) {

        log.info("Received housing tax extraction {}", event.getMetadata().getDocumentId());

        UpdateProfileCommand command = HousingTaxMapper.toCommand(event);

        updateSupportingDocumentProfileUseCase.updateSupportingDocumentProfile(
                event.getMetadata().getSubject(),
                command, event.getMetadata());
    }
}