package com.govtech.extraction.application;

import com.govtech.events.DocumentExtractionCompletedEvent;
import com.govtech.events.DocumentOCRCompletedEvent;
import com.govtech.extraction.application.prompt.PromptProvider;
import com.govtech.extraction.infrastructure.llm.exception.ExtractionException;
import com.govtech.platform.messaging.exception.MessagingException;
import com.govtech.platform.messaging.publisher.KafkaEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExtractionService {

    private final PromptProvider promptProvider;
    private final ExtractionProvider extractionProvider;
    private final KafkaEventPublisher publisher;

    public void process(DocumentOCRCompletedEvent event) {

        try {

            String prompt = promptProvider.buildPrompt(
                    event.getDocumentType(),
                    event.getText());
                    
            log.info("Starting extraction for {}", event.getDocumentId());
            String extractedData = extractionProvider.extract(prompt);

            var completed = DocumentExtractionCompletedEvent.newBuilder()
                    .setDocumentId(event.getDocumentId())
                    .setSubject(event.getSubject())
                    .setBucket(event.getBucket())
                    .setObjectKey(event.getObjectKey())
                    .setDocumentType(event.getDocumentType())
                    .setContentType(event.getContentType())
                    .setExtractedData(extractedData)
                    .setModel(extractionProvider.getModel())
                    .setConfidence(1.0)
                    .setProcessedAt(Instant.now().toString())
                    .build();

            publisher.publish(
                    "document.extraction.completed",
                    event.getObjectKey(),
                    completed);

            log.info("Extraction completed for document {}", event.getDocumentId());
        } catch (ExtractionException e) {
            log.error(
                    "LLM extraction failed for document '{}' (documentId={})",
                    event.getObjectKey(),
                    event.getDocumentId(),
                    e);
            throw e;

        } catch (MessagingException e) {
            log.error(
                    "Failed to publish extraction completion event for document '{}'",
                    event.getObjectKey(),
                    e);
            throw e;

        } catch (Exception e) {
            log.error(
                    "Unexpected error while processing document '{}'",
                    event.getObjectKey(),
                    e);
            throw new RuntimeException("Unexpected error while processing document", e);
        }
    }
}