package com.govtech.security.application.even;

import org.springframework.stereotype.Service;

import com.govtech.events.security.DocumentUploadedScanCompletedEvent;
import com.govtech.platform.messaging.publisher.EventPublisher;
import com.govtech.platform.messaging.topics.Topics;
import com.govtech.security.domain.model.SecurityScan;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentScanEventService {

        private final EventPublisher publisher;

        public void publishScanCompleted(
                        SecurityScan scan) {

                DocumentUploadedScanCompletedEvent event = DocumentUploadedScanCompletedEvent
                                .newBuilder()

                                .setDocumentId(
                                                scan.getDocumentId())

                                .setSecurityStatus(
                                                scan.getStatus().name())

                                .setScanEngine(
                                                scan.getScanEngine())

                                .setScannedAt(
                                                scan.getScannedAt().toString())

                                .setSha256(
                                                scan.getSha256())

                                .setDetectedContentType(
                                                scan.getDetectedContentType())

                                .build();

                publisher.publish(

                                Topics.DOCUMENT_UPLOADED_SCAN_COMPLETED,

                                scan.getDocumentId().toString(),

                                event

                );

        }

}