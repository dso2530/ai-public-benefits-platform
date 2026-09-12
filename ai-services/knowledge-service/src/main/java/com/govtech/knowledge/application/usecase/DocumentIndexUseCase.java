package com.govtech.knowledge.application.usecase;

import org.springframework.stereotype.Service;

import com.govtech.knowledge.application.service.KnowledgeIndexService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentIndexUseCase {

    private final KnowledgeIndexService indexService;

    public void execute(
            Long documentId,
            String source,
            String territoryCode,
            String documentType,
            String text) {

        if (documentId == null) {
            throw new IllegalArgumentException(
                    "documentId must not be null");
        }

        if (text == null || text.isBlank()) {
            log.warn(
                    "Ignoring empty document documentId={}",
                    documentId);
            return;
        }

        log.info(
                "Indexing document documentId={}, source={}, type={}",
                documentId,
                source,
                documentType);

        indexService.index(
                documentId,
                source,
                territoryCode,
                documentType,
                text);
    }
}