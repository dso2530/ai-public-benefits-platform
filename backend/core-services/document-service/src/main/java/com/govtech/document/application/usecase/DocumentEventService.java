package com.govtech.document.application.usecase;

import com.govtech.document.infrastructure.persistence.DocumentJpaEntity;
import com.govtech.events.document.DocumentUploadedEvent;
import com.govtech.events.security.DocumentUploadScanRequestedEvent;
import com.govtech.platform.messaging.publisher.EventPublisher;
import com.govtech.platform.messaging.topics.Topics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentEventService {

    private final EventPublisher publisher;

    public void publishScanRequested(DocumentJpaEntity document) {

        DocumentUploadScanRequestedEvent event = DocumentUploadScanRequestedEvent.newBuilder()
                .setDocumentId(document.getId())
                .setApplicationId(null != document.getApplicationId() ? document.getApplicationId().toString() : null)
                .setSubject(document.getSubject())
                .setBucket(document.getBucket())
                .setObjectKey(document.getObjectKey())
                .setDocumentType(document.getDocumentType().name())
                .setContentType(document.getContentType())
                .setUploadedAt(document.getUploadedAt().toString())
                .setSha256(document.getSha256())
                .build();

        publisher.publish(Topics.DOCUMENT_UPLOADED_SCAN_REQUESTED, document.getId().toString(), event);
    }

    public void publishUploaded(
            DocumentJpaEntity document) {

        DocumentUploadedEvent event = DocumentUploadedEvent.newBuilder()

                .setDocumentId(
                        document.getId())

                .setSubject(
                        document.getSubject())
                .setApplicationId(null != document.getApplicationId() ? document.getApplicationId().toString() : null)

                .setBucket(
                        document.getBucket())
                .setObjectKey(
                        document.getObjectKey())
                .setDocumentType(
                        document.getDocumentType().name())
                .setContentType(
                        document.getContentType())
                .setSha256(
                        document.getSha256())
                .setUploadedAt(
                        document.getUploadedAt().toString())
                .build();

        publisher.publish(
                Topics.DOCUMENT_UPLOADED,
                document.getId().toString(),
                event);

    }
}
