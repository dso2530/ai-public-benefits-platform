package com.govtech.profile.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.DocumentExtractionCompletedEvent;
import com.govtech.profile.application.dto.ExtractedData;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.ExtractionDataMapper;
import com.govtech.profile.application.usecase.UpdateProfileTaxUseCase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DocumentExtractedCompletedListener {
    private final ExtractionDataMapper extractionDataMapper;
    private final UpdateProfileTaxUseCase updateProfileUseCase;

    // @KafkaListener(topics = "document.extraction.completed", groupId =
    // "${messaging.kafka.group-id}")
    public void onDocumentExtracted(DocumentExtractionCompletedEvent event) {

        ExtractedData extracted = extractionDataMapper.from(event.getExtractedData());

        updateProfileUseCase.update(
                event.getSubject(),
                UpdateProfileCommand.from(extracted));
    }
}