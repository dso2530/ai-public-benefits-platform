package com.govtech.document.application.service;

import com.govtech.document.application.command.RegisterDocumentCommand;
import com.govtech.document.application.event.DocumentEventFactory;

import com.govtech.document.infrastructure.persistence.DocumentJpaEntity;
import com.govtech.document.infrastructure.persistence.DocumentJpaMapper;
import com.govtech.document.infrastructure.persistence.DocumentJpaRepository;
import com.govtech.shared.model.DocumentStatus;
import com.govtech.shared.model.SecurityStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentRegistrationService {

        private final DocumentJpaRepository repository;

        private final DocumentOutboxService outboxService;

        private final DocumentEventFactory documentEventFactory;

        private final DocumentJpaMapper documentMapper;

        @Transactional
        public DocumentJpaEntity register(
                        RegisterDocumentCommand command) {

                DocumentJpaEntity document = DocumentJpaEntity.builder()

                                .subject(
                                                command.subject())

                                .applicationId(
                                                command.applicationId())

                                .name(
                                                command.name())

                                .documentType(
                                                command.documentType())

                                .status(
                                                DocumentStatus.UPLOADED.name())

                                .securityStatus(
                                                SecurityStatus.PENDING)

                                .fileName(
                                                command.fileName())

                                .sha256(
                                                command.sha256())

                                .bucket(
                                                command.bucket())

                                .objectKey(
                                                command.objectKey())

                                .fileSize(
                                                command.fileSize())

                                .contentType(
                                                command.contentType())

                                .uploadedAt(
                                                command.uploadedAt())

                                .origin(
                                                command.origin())

                                .source(
                                                command.source())

                                .connectorName(
                                                command.connectorName())

                                .connectorType(
                                                command.connectorType().name())

                                .territoryCode(
                                                command.territoryCode())

                                .metadata(
                                                command.metadata())

                                .build();

                log.info(
                                "Registering document source={}, fileName={}, bucket={}, objectKey={}",
                                command.source(),
                                command.fileName(),
                                command.bucket(),
                                command.objectKey());

                /*
                 * Même transaction DB :
                 *
                 * 1. document
                 * 2. outbox_event
                 *
                 * Si l'un échoue, tout est rollback.
                 */
                DocumentJpaEntity saved = repository.save(document);

                outboxService.publish(
                                documentMapper.toDomain(saved),
                                documentEventFactory::buildScanRequested,
                                "DocumentScanRequested",
                                "document.scan.requested");

                return saved;
        }
}