package com.govtech.knowledge.api;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.govtech.knowledge.application.usecase.VectorSearchUseCase;
import com.govtech.knowledge.domain.model.RetrievedChunk;
import com.govtech.knowledge.api.dto.RetrievedChunkResponse;
import com.govtech.knowledge.api.dto.SearchRequest;
import com.govtech.knowledge.api.dto.SearchResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@Slf4j
public class KnowledgeSearchController {

    private final VectorSearchUseCase useCase;

    @PostMapping("/search")
    public SearchResponse search(

            @RequestBody SearchRequest request) {

        Instant start = Instant.now();

        log.info(
                "Vector search request received territoryCode={}, limit={}, embeddingDimension={}",
                request.territoryCode(),
                request.limit(),
                request.embedding() != null
                        ? request.embedding().length
                        : 0);

        try {

            List<RetrievedChunk> chunks =

                    useCase.search(

                            request.embedding(),

                            request.territoryCode(),

                            request.limit()

                    );

            long duration =

                    Duration.between(
                            start,
                            Instant.now())
                            .toMillis();

            log.info(
                    "Vector search completed territoryCode={}, results={}, durationMs={}",
                    request.territoryCode(),
                    chunks.size(),
                    duration);

            return new SearchResponse(

                    chunks.stream()

                            .map(this::toResponse)

                            .toList(),

                    chunks.size()

            );

        } catch (Exception e) {

            log.error(
                    "Vector search failed territoryCode={}, limit={}",
                    request.territoryCode(),
                    request.limit(),
                    e);

            throw e;
        }
    }

    private RetrievedChunkResponse toResponse(

            RetrievedChunk chunk) {

        log.debug(
                "Mapping retrieved chunk documentId={}, chunkNumber={}, score={}",
                chunk.documentId(),
                chunk.chunkNumber(),
                chunk.score());

        return new RetrievedChunkResponse(

                chunk.documentId(),

                chunk.chunkNumber(),

                chunk.content(),

                chunk.score(),

                chunk.metadata()

        );
    }
}