package com.govtech.document.application.usecase;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.document.domain.exception.DocumentNotFoundException;
import com.govtech.document.domain.model.SecurityStatus;
import com.govtech.document.infrastructure.persistence.DocumentJpaEntity;
import com.govtech.document.infrastructure.persistence.DocumentJpaRepository;
import com.govtech.platform.storage.service.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateDocumentSecurityStatusUseCase {

        private final DocumentJpaRepository repository;

        private final StorageService storageService;

        private final DocumentEventService eventService;

        @Transactional
        public void execute(
                        Long documentId,
                        String securityStatus,
                        String scanEngine,
                        String scannedAt,
                        String sha256,
                        String detectedContentType) {

                DocumentJpaEntity document = repository.findById(documentId)
                                .orElseThrow(
                                                () -> new DocumentNotFoundException(documentId));

                SecurityStatus scanStatus = SecurityStatus.valueOf(securityStatus);

                document.setScanEngine(scanEngine);

                document.setScannedAt(
                                Instant.parse(scannedAt));

                document.setSha256(sha256);

                document.setDetectedContentType(
                                detectedContentType);

                boolean clean = scanStatus == SecurityStatus.CLEAN;

                boolean contentTypeValid = detectedContentType != null
                                &&
                                detectedContentType.equals(
                                                document.getContentType());

                if (clean && contentTypeValid) {

                        String newObjectKey = "citizens/"
                                        + document.getSubject()
                                        + "/validated/"
                                        + document.getId()
                                        + "/"
                                        + document.getFileName();

                        storageService.move(
                                        "documents-quarantine",
                                        document.getObjectKey(),
                                        "documents",
                                        newObjectKey);

                        document.setBucket("documents");

                        document.setObjectKey(
                                        newObjectKey);

                        document.setSecurityStatus(
                                        SecurityStatus.CLEAN);

                        eventService.publishUploaded(document);

                } else {

                        document.setSecurityStatus(
                                        SecurityStatus.REJECTED);

                }

                repository.save(document);
        }
}