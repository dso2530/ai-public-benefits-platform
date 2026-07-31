package com.govtech.security.infrastructure.kafka;

import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.security.DocumentUploadScanRequestedEvent;
import com.govtech.platform.storage.service.StorageService;
import com.govtech.security.application.usecase.ScanDocumentUseCase;
import com.govtech.security.domain.exception.DocumentScanException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentUploadedScanRequestedListener {

    private final StorageService storageService;
    private final ScanDocumentUseCase scanDocumentUseCase;

    @KafkaListener(topics = "document.uploaded.scan.requested", groupId = "${messaging.kafka.group-id}")
    public void onMessage(
            DocumentUploadScanRequestedEvent event) {

        log.info(
                "Document scan requested documentId={}",
                event.getDocumentId());

        try {

            byte[] content = storageService.download(
                    event.getBucket(),
                    event.getObjectKey()).readAllBytes();

            scanDocumentUseCase.execute(
                    event.getDocumentId(),
                    content,
                    event.getSha256());

        } catch (IOException e) {

            log.error(
                    "Unable to download document {} from storage",
                    event.getDocumentId(),
                    e);

            throw new DocumentScanException(
                    "Storage download failed",
                    e);
        }

    }
}