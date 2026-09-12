package com.govtech.assistant.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.govtech.assistant.api.dto.AssistantQueryRequest;
import com.govtech.assistant.api.dto.AssistantResponse;
import com.govtech.assistant.api.dto.AssistantSourceDto;
import com.govtech.assistant.client.RagClient;
import com.govtech.assistant.client.dto.RagQueryRequest;
import com.govtech.assistant.client.dto.RagResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

}