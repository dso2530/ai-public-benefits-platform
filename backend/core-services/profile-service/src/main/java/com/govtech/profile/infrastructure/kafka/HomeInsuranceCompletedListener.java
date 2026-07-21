package com.govtech.profile.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.homeinsurance.HomeInsuranceExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.HomeInsuranceMapper;
import com.govtech.profile.application.usecase.UpdateSupportingDocumentProfileUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HomeInsuranceCompletedListener {

    private final UpdateSupportingDocumentProfileUseCase updateSupportingDocumentProfileUseCase;

    @KafkaListener(topics = "document.home-insurance.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(HomeInsuranceExtractionCompletedEvent event) {

        log.info("Received home insurance extraction {}", event.getMetadata().getDocumentId());

        UpdateProfileCommand command = HomeInsuranceMapper.toCommand(event);

        updateSupportingDocumentProfileUseCase.updateSupportingDocumentProfile(
                event.getMetadata().getSubject(),
                command, event.getMetadata());
    }
}