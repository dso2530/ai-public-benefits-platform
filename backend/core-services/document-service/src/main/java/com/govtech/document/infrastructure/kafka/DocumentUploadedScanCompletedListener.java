package com.govtech.document.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.document.application.usecase.UpdateDocumentSecurityStatusUseCase;
import com.govtech.events.security.DocumentUploadedScanCompletedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentUploadedScanCompletedListener {

        private final UpdateDocumentSecurityStatusUseCase useCase;

        @KafkaListener(topics = "document.uploaded.scan.completed", groupId = "document-service")
        @Transactional
        public void consume(
                        DocumentUploadedScanCompletedEvent event) {

                log.info(
                                "Security scan completed documentId={}, status={}",
                                event.getDocumentId(),
                                event.getSecurityStatus());

                useCase.execute(
                                event.getDocumentId(),
                                event.getSecurityStatus(),
                                event.getScanEngine(),
                                event.getScannedAt(),
                                event.getSha256(),
                                event.getDetectedContentType());

        }

}