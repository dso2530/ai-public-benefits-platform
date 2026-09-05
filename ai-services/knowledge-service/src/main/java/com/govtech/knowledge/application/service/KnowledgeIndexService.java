package com.govtech.knowledge.application.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.knowledge.domain.model.DocumentChunk;
import com.govtech.knowledge.domain.model.EmbeddingVector;
import com.govtech.knowledge.domain.port.VectorStorePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeIndexService {

    private final KnowledgeDocumentService documentService;

    private final ChunkingService chunkingService;

    private final EmbeddingService embeddingService;

    private final VectorStorePort vectorStore;

    @Transactional
    public void index(
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
                    "Skipping indexing of empty document documentId={}",
                    documentId);
            return;
        }

        /*
         * Création / récupération du document métier.
         */
        documentService.getOrCreate(
                documentId,
                source,
                territoryCode,
                documentType);

        /*
         * Métadonnées génériques.
         *
         * Aucun couplage avec CAF ou un domaine métier particulier.
         */
        Map<String, String> metadata =
                buildMetadata(
                        source,
                        territoryCode,
                        documentType);

        log.info(
                "Starting indexing documentId={}, source={}, documentType={}, textSize={}",
                documentId,
                source,
                documentType,
                text.length());

        /*
         * Le ChunkingService est responsable uniquement
         * de la qualité des chunks :
         *
         * - normalisation
         * - paragraphes
         * - phrases
         * - fallback par taille
         * - overlap
         *
         * Les métadonnées sont simplement propagées.
         */
        chunkingService.process(
                documentId,
                text,
                metadata,
                this::indexChunk);

        log.info(
                "Document indexed documentId={}",
                documentId);
    }

    private Map<String, String> buildMetadata(
            String source,
            String territoryCode,
            String documentType) {

        Map<String, String> metadata =
                new HashMap<>();

        metadata.put(
                "source",
                valueOrEmpty(source));

        metadata.put(
                "territoryCode",
                valueOrEmpty(territoryCode));

        metadata.put(
                "documentType",
                valueOrEmpty(documentType));

        return Map.copyOf(metadata);
    }

    private void indexChunk(
            DocumentChunk chunk) {

        if (chunk == null
                || chunk.content() == null
                || chunk.content().isBlank()) {

            log.warn(
                    "Skipping empty chunk documentId={}, chunk={}",
                    chunk != null
                            ? chunk.documentId()
                            : null,
                    chunk != null
                            ? chunk.chunkNumber()
                            : null);

            return;
        }

        log.debug(
                "Embedding chunk documentId={}, chunk={}, size={}",
                chunk.documentId(),
                chunk.chunkNumber(),
                chunk.content().length());

        EmbeddingVector embedding =
                embeddingService.generate(
                        chunk.content());

        vectorStore.save(
                chunk,
                embedding);
    }

    private String valueOrEmpty(
            String value) {

        return value != null
                ? value
                : "";
    }
}