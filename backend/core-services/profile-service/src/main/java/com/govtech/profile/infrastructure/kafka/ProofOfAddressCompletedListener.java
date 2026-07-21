package com.govtech.profile.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.proofofaddress.ProofOfAddressExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.ProofOfAddressMapper;
import com.govtech.profile.application.usecase.UpdateSupportingDocumentProfileUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProofOfAddressCompletedListener {

    private final UpdateSupportingDocumentProfileUseCase updateSupportingDocumentProfileUseCase;

    @KafkaListener(topics = "document.proof-of-address.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(ProofOfAddressExtractionCompletedEvent event) {

        log.info("Received proof of address extraction {}", event.getMetadata().getDocumentId());

        UpdateProfileCommand command = ProofOfAddressMapper.toCommand(event);

        updateSupportingDocumentProfileUseCase.updateSupportingDocumentProfile(
                event.getMetadata().getSubject(),
                command,event.getMetadata());
    }
}