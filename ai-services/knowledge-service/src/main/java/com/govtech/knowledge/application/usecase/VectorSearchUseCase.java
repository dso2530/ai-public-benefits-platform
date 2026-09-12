package com.govtech.knowledge.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.knowledge.domain.model.RetrievedChunk;
import com.govtech.knowledge.domain.port.VectorSearchPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VectorSearchUseCase {

        private final VectorSearchPort vectorSearchPort;

        public List<RetrievedChunk> search(
                        float[] embedding,
                        String territoryCode,
                        int limit) {

                if (embedding == null || embedding.length == 0) {
                        throw new IllegalArgumentException(
                                        "embedding must not be null or empty");
                }

                if (limit <= 0) {
                        throw new IllegalArgumentException(
                                        "limit must be greater than 0");
                }

                log.info(
                                "Starting vector search territory={}, limit={}, dimension={}",
                                territoryCode,
                                limit,
                                embedding.length);

                List<RetrievedChunk> results = vectorSearchPort.search(
                                embedding,
                                territoryCode,
                                limit);

                log.info(
                                "Vector search completed territory={}, results={}",
                                territoryCode,
                                results.size());

                return results;
        }
}