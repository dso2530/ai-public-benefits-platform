package com.govtech.ocr.application;

import java.io.InputStream;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.govtech.events.DocumentOCRCompletedEvent;
import com.govtech.events.DocumentUploadedEvent;
import com.govtech.ocr.infrastructure.ocr.exception.OCRException;
import com.govtech.platform.messaging.exception.MessagingException;
import com.govtech.platform.messaging.publisher.KafkaEventPublisher;
import com.govtech.platform.storage.exception.StorageException;
import com.govtech.platform.storage.service.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OCRService {

    private final StorageService storageService;
    private final OCRProvider ocrProvider;
    private final KafkaEventPublisher publisher;

    public void process(DocumentUploadedEvent event) {

        try (InputStream stream = storageService.download(
                event.getObjectKey())) {

            String text = ocrProvider.extractText(stream);

            var completed = DocumentOCRCompletedEvent.newBuilder()
                    .setDocumentId(event.getDocumentId())
                    .setSubject(event.getSubject())
                    .setBucket(event.getBucket())
                    .setObjectKey(event.getObjectKey())
                    .setDocumentType(event.getDocumentType())
                    .setContentType(event.getContentType())
                    .setText(text)
                    .setProcessedAt(Instant.now().toString())
                    .build();

            publisher.publish(
                    "document.ocr.completed", event.getObjectKey(),
                    completed);

        } catch (StorageException e) {
            log.error("Failed to download document '{}'", event.getObjectKey(), e);
            throw e;

        } catch (OCRException e) {
            log.error(
                    "OCR extraction failed for document '{}' (documentId={})",
                    event.getObjectKey(),
                    event.getDocumentId(),
                    e);
            throw e;

        } catch (MessagingException e) {
            log.error("Failed to publish OCR completion event for document '{}'", event.getObjectKey(), e);
            throw e;

        } catch (Exception e) {
            log.error("Unexpected error while processing document '{}'", event.getObjectKey(), e);
            throw new RuntimeException("Unexpected error while processing document", e);
        }
    }
}