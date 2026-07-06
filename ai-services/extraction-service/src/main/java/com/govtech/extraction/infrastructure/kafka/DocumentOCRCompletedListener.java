package com.govtech.extraction.infrastructure.kafka;

import com.govtech.events.DocumentOCRCompletedEvent;
import com.govtech.extraction.application.ExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentOCRCompletedListener {

    private final ExtractionService extractionService;

    @KafkaListener(topics = "document.ocr.completed", groupId = "${messaging.kafka.group-id}")
    public void consume(DocumentOCRCompletedEvent event) {
        log.info("Received OCR completed event for document {}", event.getDocumentId());
        extractionService.process(event);
    }
}