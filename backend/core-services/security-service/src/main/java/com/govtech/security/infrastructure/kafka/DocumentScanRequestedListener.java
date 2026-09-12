package com.govtech.security.infrastructure.kafka;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.common.BaseEvent;
import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.events.security.DocumentScanRequestedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.platform.messaging.idempotency.IdempotencyService;
import com.govtech.platform.storage.service.StorageService;
import com.govtech.security.application.command.DocumentScanRequest;
import com.govtech.security.application.usecase.ScanDocumentUseCase;
import com.govtech.security.domain.exception.DocumentScanException;
import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentScanRequestedListener {

        private static final String TOPIC = "document.scan.requested";

        private final StorageService storageService;
        private final ScanDocumentUseCase scanDocumentUseCase;
        private final IdempotencyService idempotencyService;

        @Value("${messaging.kafka.group-id}")
        private String consumerGroup;

        @KafkaListener(topics = TOPIC, groupId = "${messaging.kafka.group-id}")
        public void onMessage(DocumentScanRequestedEvent event) {

                var documentBase = event.getBaseEvent();
                var base = documentBase.getBase();

                validateEvent(event);

                Long documentId = documentBase.getDocumentId();
                String eventId = base.getEventId();
                String correlationId = base.getCorrelationId();

                log.info(
                                "Document scan requested documentId={} eventId={} correlationId={} causationId={}",
                                documentId,
                                eventId,
                                correlationId,
                                base.getCausationId());

                if (!idempotencyService.tryAcquire(consumerGroup, eventId)) {

                        log.info(
                                        "Duplicate DocumentScanRequestedEvent ignored "
                                                        + "documentId={} eventId={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        consumerGroup);

                        return;
                }

                try {

                        byte[] content = downloadDocument(
                                        documentBase.getBucket(),
                                        documentBase.getObjectKey(),
                                        documentId);

                        EventContext eventContext = new EventContext(
                                        correlationId,
                                        eventId);

                        scanDocumentUseCase.execute(
                                        DocumentScanRequest.builder()
                                                        .documentId(documentBase.getDocumentId())
                                                        .content(content)
                                                        .sha256(documentBase.getSha256())
                                                        .origin(
                                                                        DocumentOrigin.valueOf(
                                                                                        documentBase.getOrigin()
                                                                                                        .name()))
                                                        .bucket(documentBase.getBucket())
                                                        .objectKey(documentBase.getObjectKey())
                                                        .contentType(documentBase.getContentType())
                                                        .fileName(documentBase.getFileName())
                                                        .documentType(
                                                                        DocumentType.valueOf(
                                                                                        documentBase.getDocumentType()
                                                                                                        .name()))
                                                        .metadata(event.getMetadata())
                                                        .eventContext(eventContext)
                                                        .build());

                        idempotencyService.markProcessed(
                                        consumerGroup,
                                        eventId);

                        log.info(
                                        "DocumentScanRequestedEvent processed successfully "
                                                        + "documentId={} eventId={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        consumerGroup);

                } catch (Exception e) {

                        idempotencyService.release(
                                        consumerGroup,
                                        eventId);

                        log.error(
                                        "Failed to process DocumentScanRequestedEvent "
                                                        + "documentId={} eventId={} consumerGroup={}",
                                        documentId,
                                        eventId,
                                        consumerGroup,
                                        e);

                        throw e;
                }
        }

        private void validateEvent(DocumentScanRequestedEvent event) {

                if (event == null) {
                        throw new IllegalStateException(
                                        "DocumentScanRequestedEvent must not be null");
                }

                DocumentBaseEvent documentBase = event.getBaseEvent();

                if (documentBase == null) {
                        throw new IllegalStateException(
                                        "DocumentBaseEvent is missing");
                }

                BaseEvent base = documentBase.getBase();

                if (base == null) {
                        throw new IllegalStateException(
                                        "BaseEvent is missing");
                }

                String eventId = base.getEventId();

                if (eventId == null || eventId.isBlank()) {
                        throw new IllegalStateException(
                                        "Event ID is missing for DocumentScanRequestedEvent");
                }
        }

        private byte[] downloadDocument(
                        String bucket,
                        String objectKey,
                        Long documentId) {

                try {

                        return storageService
                                        .download(bucket, objectKey)
                                        .readAllBytes();

                } catch (IOException e) {

                        throw new DocumentScanException(
                                        "Unable to download document "
                                                        + documentId
                                                        + " from storage",
                                        e);
                }
        }
}