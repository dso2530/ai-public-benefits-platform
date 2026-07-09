package com.govtech.profile.infrastructure.kafka;

import com.govtech.events.tax.TaxNoticeExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.TaxNoticeProfileMapper;
import com.govtech.profile.application.usecase.UpdateProfileTaxUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaxNoticeCompletedListener {

    private final UpdateProfileTaxUseCase updateProfileTaxUseCase;

    @KafkaListener(topics = "document.tax-notice.extraction.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(TaxNoticeExtractionCompletedEvent event) {

        log.info(
                "Received tax notice extraction for document {}",
                event.getMetadata().getDocumentId());

        UpdateProfileCommand updateProfileCommand = TaxNoticeProfileMapper.toCommand(event);

        updateProfileTaxUseCase.update(event.getMetadata().getSubject(), updateProfileCommand);
    }
}