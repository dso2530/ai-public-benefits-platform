package com.govtech.document.application.usecase;

import com.govtech.document.infrastructure.persistence.DocumentJpaEntity;
import com.govtech.events.DocumentUploadedEvent;
import com.govtech.platform.messaging.publisher.EventPublisher;
import com.govtech.platform.messaging.topics.Topics;
import com.govtech.platform.storage.config.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentEventService {

  private final EventPublisher publisher;
  private final StorageProperties storageProperties;

  public void publishUploaded(DocumentJpaEntity document) {

    DocumentUploadedEvent event = DocumentUploadedEvent.newBuilder()
        .setDocumentId(document.getId())
        .setApplicationId(null != document.getApplicationId() ? document.getApplicationId().toString() : null)
        .setSubject(document.getSubject())
        .setBucket(storageProperties.bucket())
        .setObjectKey(document.getObjectKey())
        .setDocumentType(document.getDocumentType().name())
        .setContentType(document.getContentType())
        .setUploadedAt(document.getUploadedAt().toString())
        .build();

    publisher.publish(Topics.DOCUMENT_UPLOADED, document.getId().toString(), event);
  }
}
