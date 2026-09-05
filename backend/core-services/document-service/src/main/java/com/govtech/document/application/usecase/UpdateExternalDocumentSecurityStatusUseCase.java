package com.govtech.document.application.usecase;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.document.application.command.UpdateDocumentSecurityCommand;
import com.govtech.document.application.event.DocumentEventFactory;
import com.govtech.document.application.service.DocumentOutboxService;
import com.govtech.document.application.service.DocumentStorageMoveService;
import com.govtech.document.domain.exception.DocumentNotFoundException;
import com.govtech.document.infrastructure.persistence.DocumentJpaEntity;
import com.govtech.document.infrastructure.persistence.DocumentJpaMapper;
import com.govtech.document.infrastructure.persistence.DocumentJpaRepository;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.shared.model.SecurityStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateExternalDocumentSecurityStatusUseCase {

        private static final String QUARANTINE_BUCKET = "documents-ai-quarantine";

        private static final String DOCUMENT_BUCKET = "documents-ai";

        private final DocumentJpaRepository repository;
        private final DocumentStorageMoveService storageMoveService;
        private final DocumentOutboxService outboxService;
        private final DocumentEventFactory documentEventFactory;
        private final DocumentJpaMapper documentJpaMapper;

        @Transactional
        public void execute(
                        UpdateDocumentSecurityCommand command,
                        EventContext eventContext) {

                DocumentJpaEntity document = repository.findById(command.documentId())
                                .orElseThrow(
                                                () -> new DocumentNotFoundException(
                                                                command.documentId()));

                SecurityStatus status = SecurityStatus.valueOf(command.securityStatus());

                updateScanMetadata(document, command);

                if (status == SecurityStatus.CLEAN) {

                        handleCleanDocument(
                                        document,
                                        eventContext);

                } else {

                        document.setSecurityStatus(
                                        SecurityStatus.REJECTED);

                        repository.save(document);
                }
        }

        private void updateScanMetadata(
                        DocumentJpaEntity document,
                        UpdateDocumentSecurityCommand command) {

                document.setScanEngine(command.scanEngine());
                document.setScannedAt(
                                Instant.parse(command.scannedAt()));
                document.setSha256(command.sha256());
                document.setDetectedContentType(
                                command.detectedContentType());
        }

        private void handleCleanDocument(
                        DocumentJpaEntity document,
                        EventContext eventContext) {

                String destinationObjectKey = buildExternalObjectKey(document);

                storageMoveService.moveIfNecessary(
                                document.getId(),
                                QUARANTINE_BUCKET,
                                document.getObjectKey(),
                                DOCUMENT_BUCKET,
                                destinationObjectKey);

                document.setBucket(DOCUMENT_BUCKET);
                document.setObjectKey(destinationObjectKey);
                document.setSecurityStatus(SecurityStatus.CLEAN);

                DocumentJpaEntity saved = repository.save(document);

                outboxService.publish(
                                documentJpaMapper.toDomain(saved),
                                eventContext,
                                documentEventFactory::buildUploaded,
                                "DocumentUploaded",
                                "document.uploaded");
        }

        private String buildExternalObjectKey(
                        DocumentJpaEntity document) {

                return "external/"
                                + document.getSource()
                                + "/"
                                + document.getId()
                                + "/"
                                + document.getFileName();
        }
}