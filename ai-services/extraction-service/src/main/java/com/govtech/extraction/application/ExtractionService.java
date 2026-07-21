package com.govtech.extraction.application;

import com.govtech.extraction.application.event.DocumentEventFactory;
import com.govtech.extraction.application.prompt.PromptProvider;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.infrastructure.llm.exception.ExtractionException;
import com.govtech.platform.messaging.exception.MessagingException;
import com.govtech.platform.messaging.publisher.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExtractionService {

    private final PromptProvider promptProvider;
    private final ExtractionProvider extractionProvider;
    private final DocumentEventFactory documentEventFactory;
    private final EventPublisher eventPublisher;

    public void process(Document document) {

        try {

            String prompt = promptProvider.buildPrompt(
                    document.type().promptName(),
                    document.text());

            log.info("Starting extraction for {}", document.documentId());
            String extractedData = extractionProvider.extract(prompt);

            Object event = documentEventFactory.create(
                    document,
                    extractedData,
                    extractionProvider.getModel());

            eventPublisher.publish(
                    document.type().topic(),
                    document.objectKey(),
                    event);

            log.info("Extraction completed for document {}", document.documentId());
        } catch (ExtractionException e) {
            log.error(
                    "LLM extraction failed for document '{}' (documentId={})",
                    document.objectKey(),
                    document.documentId(),
                    e);
            throw e;

        } catch (MessagingException e) {
            log.error(
                    "Failed to publish extraction completion event for document '{}'",
                    document.objectKey(),
                    e);
            throw e;

        } catch (Exception e) {
            log.error(
                    "Unexpected error while processing document '{}'",
                    document.objectKey(),
                    e);
            throw new RuntimeException("Unexpected error while processing document", e);
        }
    }
}