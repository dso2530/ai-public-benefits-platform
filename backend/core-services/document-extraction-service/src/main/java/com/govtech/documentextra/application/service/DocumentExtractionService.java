package com.govtech.documentextra.application.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.documentextra.application.command.DocumentExtractionCommand;
import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.documentextra.domain.port.DocumentExtractor;

import com.govtech.platform.storage.service.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentExtractionService {

        private final StorageService storageService;
        private final List<DocumentExtractor> extractors;

        public ExtractionResult extract(
                        DocumentExtractionCommand command) {

                try (
                                InputStream stream = storageService.download(
                                                command.bucket(),
                                                command.objectKey())) {

                        DocumentExtractor extractor = extractors.stream()
                                        .filter(e -> e.supports(command.contentType()))
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "No extractor found for "
                                                                        + command.contentType()));

                        ExtractionResult result = extractor.extract(stream);

                        log.info(
                                        "Document extraction completed documentId={} extractor={}",
                                        command.documentId(),
                                        extractor.getClass().getSimpleName());

                        return result;

                } catch (Exception e) {

                        log.error(
                                        "Document extraction failed documentId={}",
                                        command.documentId(),
                                        e);

                        throw new RuntimeException(
                                        "Document extraction failed",
                                        e);
                }
        }
}