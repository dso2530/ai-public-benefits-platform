package com.govtech.citizen.assistant.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.govtech.citizen.assistant.dto.AssistantQueryRequest;
import com.govtech.citizen.assistant.dto.AssistantResponse;
import com.govtech.citizen.assistant.dto.AssistantSourceDto;
import com.govtech.citizen.assistant.dto.RagQueryRequest;
import com.govtech.citizen.assistant.dto.RagResponse;
import com.govtech.citizen.clients.RagClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssistantService {

        private final RagClient ragClient;

        public AssistantResponse ask(
                        AssistantQueryRequest request) {

                Instant start = Instant.now();

                log.info(
                                "Assistant query received territoryCode={}, questionLength={}",
                                request.territoryCode(),
                                request.question().length());

                try {

                        RagResponse response =

                                        ragClient.query(

                                                        new RagQueryRequest(

                                                                        request.question(),

                                                                        request.territoryCode()

                                                        )

                                        );

                        long duration =

                                        Duration.between(
                                                        start,
                                                        Instant.now()).toMillis();

                        log.info(
                                        "Assistant response generated territoryCode={}, sources={}, durationMs={}",
                                        request.territoryCode(),
                                        response.sources() != null
                                                        ? response.sources().size()
                                                        : 0,
                                        duration);

                        return new AssistantResponse(

                                        response.answer(),

                                        response.sources()

                                                        .stream()

                                                        .map(source -> {

                                                                log.debug(
                                                                                "Mapping RAG source documentId={}, chunkNumber={}, score={}",
                                                                                source.documentId(),
                                                                                source.chunkNumber(),
                                                                                source.score());

                                                                return new AssistantSourceDto(

                                                                                source.documentId(),

                                                                                source.chunkNumber(),

                                                                                source.score(),

                                                                                source.content()

                                                        );

                                                        })

                                                        .toList()

                        );

                } catch (Exception e) {

                        log.error(
                                        "Assistant query failed territoryCode={}, questionLength={}",
                                        request.territoryCode(),
                                        request.question().length(),
                                        e);

                        throw e;

                }

        }

        /**
         * * Mode streaming. * * Le BFF transmet directement les chunks * reçus du
         * service RAG.
         */
        public Flux<String> askStream(AssistantQueryRequest request) {
                Instant start = Instant.now();
                log.info("Assistant streaming query received territoryCode={}, questionLength={}",
                                request.territoryCode(), request.question().length());
                RagQueryRequest ragRequest = new RagQueryRequest(request.question(), request.territoryCode());
                return ragClient.queryStream(ragRequest)
                                .doOnSubscribe(subscription -> log.info("Assistant streaming started territoryCode={}",
                                                request.territoryCode()))
                                .doOnNext(chunk -> log.debug("Assistant streaming chunk received length={}",
                                                chunk != null ? chunk.length() : 0))
                                .doOnComplete(() -> {
                                        long duration = Duration.between(start, Instant.now()).toMillis();
                                        log.info("Assistant streaming completed territoryCode={}, durationMs={}",
                                                        request.territoryCode(), duration);
                                }).doOnError(error -> log.error("Assistant streaming failed territoryCode={}",
                                                request.territoryCode(), error));
        }

}