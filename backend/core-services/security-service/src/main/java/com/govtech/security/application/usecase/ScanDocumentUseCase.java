package com.govtech.security.application.usecase;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.security.application.command.DocumentScanRequest;
import com.govtech.security.application.event.DocumentScanEventService;
import com.govtech.security.domain.model.DocumentAnalysis;
import com.govtech.security.domain.model.ScanResult;
import com.govtech.security.domain.model.SecurityScan;
import com.govtech.security.domain.port.DocumentAnalyzerPort;
import com.govtech.security.domain.port.MalwareScannerPort;
import com.govtech.security.domain.port.SecurityScanRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScanDocumentUseCase {

        private final MalwareScannerPort scanner;
        private final SecurityScanRepositoryPort repository;
        private final DocumentScanEventService eventService;
        private final DocumentAnalyzerPort analyzer;

        @Transactional
        public SecurityScan execute(DocumentScanRequest request) {

                log.info(
                                "Scanning document documentId={} sha256={} correlationId={} causationId={}",
                                request.getDocumentId(),
                                request.getSha256(),
                                request.getEventContext().correlationId(),
                                request.getEventContext().causationId());

                var existing = repository.findLatestByDocumentId(
                                request.getDocumentId());

                if (existing.isPresent()) {

                        log.info(
                                        "Security scan already exists documentId={} scanId={}",
                                        request.getDocumentId(),
                                        existing.get().getId());

                        return existing.get();
                }

                DocumentAnalysis analysis = analyzer.analyze(request.getContent());

                ScanResult result = scanner.scan(request.getContent());

                SecurityScan scan = SecurityScan.builder()
                                .id(UUID.randomUUID())
                                .documentId(request.getDocumentId())
                                .sha256(request.getSha256())
                                .status(result.status())
                                .scanEngine(result.engine())
                                .detectedContentType(
                                                analysis.detectedContentType())
                                .scannedAt(Instant.now())
                                .origin(request.getOrigin())

                                .bucket(request.getBucket())
                                .objectKey(request.getObjectKey())
                                .contentType(request.getContentType())
                                .fileName(request.getFileName())
                                .documentType(request.getDocumentType())

                                .build();

                SecurityScan saved = repository.save(scan);

                eventService.publishScanCompleted(
                                saved,
                                request.getMetadata(),
                                request.getEventContext());

                return saved;
        }
}