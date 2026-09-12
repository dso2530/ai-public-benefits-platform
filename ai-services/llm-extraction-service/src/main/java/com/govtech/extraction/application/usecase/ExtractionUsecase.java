package com.govtech.extraction.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.extraction.application.event.ExtractionEventService;
import com.govtech.extraction.application.port.ExtractionProvider;
import com.govtech.extraction.application.prompt.PromptProvider;
import com.govtech.extraction.domain.model.Document;
import com.govtech.platform.messaging.event.EventContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExtractionUsecase {

    private final PromptProvider promptProvider;
    private final ExtractionProvider extractionProvider;
    private final ExtractionEventService eventService;

    @Transactional
    public void execute(
            Document document,
            EventContext eventContext) {

        log.info(
                "Starting extraction for documentId={}",
                document.documentId());

        try {

            String prompt = promptProvider.buildPrompt(
                    document.type().promptName(),
                    document.text());

            String extractedData = extractionProvider.extract(prompt);

            String model = extractionProvider.getModel();

            eventService.publishExtractionCompleted(
                    document,
                    extractedData,
                    model,
                    eventContext);

            log.info(
                    "Extraction completed for documentId={}",
                    document.documentId());

        } catch (Exception e) {

            log.error(
                    "Extraction failed for documentId={} objectKey={}",
                    document.documentId(),
                    document.objectKey(),
                    e);

            throw e;
        }
    }
}