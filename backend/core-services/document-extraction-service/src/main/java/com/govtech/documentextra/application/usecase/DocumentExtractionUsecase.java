package com.govtech.documentextra.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.documentextra.application.command.DocumentExtractionCommand;
import com.govtech.documentextra.application.event.DocumentExtractionEventService;
import com.govtech.documentextra.application.service.DocumentExtractionService;
import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.platform.messaging.event.EventContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentExtractionUsecase {

    private final DocumentExtractionService extractionService;
    private final DocumentExtractionEventService eventService;

    public void process(
            DocumentExtractionCommand command,
            EventContext eventContext) {

        ExtractionResult result = extractionService.extract(command);

        eventService.publishExtractionCompleted(
                command,
                result,
                eventContext);
    }
}