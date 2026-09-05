package com.govtech.knowledge.infrastructure.vector;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.knowledge.domain.model.DocumentChunk;
import com.govtech.knowledge.domain.model.EmbeddingVector;
import com.govtech.knowledge.domain.model.RetrievedChunk;
import com.govtech.knowledge.domain.port.VectorSearchPort;
import com.govtech.knowledge.domain.port.VectorStorePort;
import com.govtech.knowledge.infrastructure.persistence.KnowledgeChunkJpaEntity;
import com.govtech.knowledge.infrastructure.persistence.KnowledgeChunkJpaRepository;
import com.govtech.knowledge.infrastructure.persistence.KnowledgeDocumentJpaEntity;
import com.govtech.knowledge.infrastructure.persistence.KnowledgeDocumentJpaRepository;
import com.govtech.knowledge.infrastructure.persistence.PgVectorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PgVectorAdapter implements VectorStorePort, VectorSearchPort {

        private final KnowledgeChunkJpaRepository chunkRepository;

        private final KnowledgeDocumentJpaRepository documentRepository;

        private final PgVectorRepository vectorRepository;

        @Override
        @Transactional
        public void save(

                        DocumentChunk chunk,

                        EmbeddingVector embedding) {

                KnowledgeDocumentJpaEntity document = documentRepository.findByDocumentId(
                                chunk.documentId())

                                .orElseThrow(() -> new IllegalStateException(
                                                "Knowledge document not found: "
                                                                + chunk.documentId()));

                KnowledgeChunkJpaEntity entity = chunkRepository.save(

                                KnowledgeChunkJpaEntity.builder()

                                                .document(document)

                                                .chunkNumber(
                                                                chunk.chunkNumber())

                                                .content(
                                                                chunk.content())

                                                .metadata(
                                                                chunk.metadata())

                                                .createdAt(
                                                                Instant.now())

                                                .build());

                vectorRepository.saveEmbedding(

                                entity.getId(),

                                embedding.values());

                log.debug(
                                "Chunk indexed documentId={}, chunkNumber={}, chunkId={}",
                                chunk.documentId(),
                                chunk.chunkNumber(),
                                entity.getId());

        }

        @Override
        public List<RetrievedChunk> search(

                        float[] embedding,

                        String territoryCode,

                        int limit) {

                log.info(
                                "Searching vectors territory={}, limit={}",
                                territoryCode,
                                limit);

                return vectorRepository.search(
                                embedding,
                                territoryCode,
                                limit);
        }

}