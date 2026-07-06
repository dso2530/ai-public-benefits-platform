package com.govtech.ocr.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.DocumentUploadedEvent;
import com.govtech.ocr.application.OCRService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DocumentUploadedListener {

    private final OCRService ocrService;

    @KafkaListener(topics = "document.uploaded", groupId = "${messaging.kafka.group-id}")
    public void consume(DocumentUploadedEvent event) {
        System.out.println(">>> MESSAGE RECU : " + event);
        ocrService.process(event);

    }
}