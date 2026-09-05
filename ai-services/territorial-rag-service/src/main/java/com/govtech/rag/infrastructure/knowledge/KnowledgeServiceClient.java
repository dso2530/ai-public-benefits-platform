package com.govtech.rag.infrastructure.knowledge;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.rag.domain.model.RetrievedChunk;
import com.govtech.rag.domain.port.KnowledgeSearchPort;
import com.govtech.rag.infrastructure.knowledge.dto.RetrievedChunkResponse;
import com.govtech.rag.infrastructure.knowledge.dto.SearchRequest;
import com.govtech.rag.infrastructure.knowledge.dto.SearchResponse;
import com.govtech.rag.infrastructure.knowledge.exception.KnowledgeSearchException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class KnowledgeServiceClient
        implements KnowledgeSearchPort {


    private final RestClient restClient;


    @Override
    public List<RetrievedChunk> search(

            float[] embedding,

            String territoryCode,

            int limit) {


        log.info(
                "Calling knowledge-service search territory={}, limit={}, embeddingDimension={}",
                territoryCode,
                limit,
                embedding.length);


        Instant start = Instant.now();


        try {


            SearchRequest request =
                    new SearchRequest(
                            embedding,
                            territoryCode,
                            limit);



            SearchResponse response =

                    restClient.post()

                            .uri("/internal/search")

                            .body(request)

                            .retrieve()

                            .body(SearchResponse.class);



            if (response == null
                    || response.chunks() == null
                    || response.chunks().isEmpty()) {


                log.warn(
                        "knowledge-service returned no chunks territory={}",
                        territoryCode);


                return List.of();
            }



            long duration =
                    Duration.between(
                            start,
                            Instant.now())
                            .toMillis();



            log.info(
                    "knowledge-service returned {} chunks in {} ms",
                    response.count(),
                    duration);



            return response.chunks()

                    .stream()

                    .map(this::toDomain)

                    .toList();



        } catch (Exception e) {


            log.error(
                    "Knowledge-service search failed territory={}",
                    territoryCode,
                    e);


            throw new KnowledgeSearchException(
                    "Unable to query knowledge-service",
                    e);
        }
    }



    private RetrievedChunk toDomain(

            RetrievedChunkResponse dto) {


        return new RetrievedChunk(          

                dto.documentId(),

                dto.chunkNumber(),

                dto.content(),

                dto.score(),

                dto.metadata()

        );
    }
}