package com.govtech.security.application.usecase;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.govtech.security.application.even.DocumentScanEventService;
import com.govtech.security.domain.model.SecurityScan;
import com.govtech.security.domain.port.DocumentAnalysis;
import com.govtech.security.domain.port.DocumentAnalyzerPort;
import com.govtech.security.domain.port.MalwareScannerPort;
import com.govtech.security.domain.port.SecurityScanRepositoryPort;
import com.govtech.security.domain.port.ScanResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScanDocumentUseCase {

        private final MalwareScannerPort scanner;

        private final SecurityScanRepositoryPort repository;

        private final DocumentScanEventService eventService;

        private final DocumentAnalyzerPort analyzer;

        public SecurityScan execute(
                        Long documentId,
                        byte[] content,
                        String sha256) {

                DocumentAnalysis analysis = analyzer.analyze(content);

                ScanResult result = scanner.scan(content);

                SecurityScan scan = SecurityScan.builder()

                                .id(UUID.randomUUID())

                                .documentId(documentId)

                                .sha256(sha256)

                                .status(result.status())

                                .scanEngine(result.engine())

                                .detectedContentType(analysis.detectedContentType())

                                .scannedAt(Instant.now())

                                .build();

                SecurityScan saved = repository.save(scan);

                eventService.publishScanCompleted(
                                saved);

                return saved;

        }

}