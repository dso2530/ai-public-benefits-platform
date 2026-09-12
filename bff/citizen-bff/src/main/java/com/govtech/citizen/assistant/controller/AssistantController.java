package com.govtech.citizen.assistant.controller;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;

import com.govtech.citizen.assistant.dto.AssistantQueryRequest;
import com.govtech.citizen.assistant.dto.AssistantResponse;
import com.govtech.citizen.assistant.service.AssistantService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
@Slf4j
public class AssistantController {

    private final AssistantService service;

    @PostMapping("/query")
    public AssistantResponse query(
            @Valid @RequestBody AssistantQueryRequest request) {

        log.info("[ASSISTANT] =============================");
        log.info("[ASSISTANT] Incoming query");
        log.info("[ASSISTANT] Question: {}", request.question());
        log.info("[ASSISTANT] Territory code: {}", request.territoryCode());

        try {

            AssistantResponse response = service.ask(request);

            log.info("[ASSISTANT] Query processed successfully");

            if (response != null) {
                log.info(
                        "[ASSISTANT] Answer length: {}",
                        response.answer() != null
                                ? response.answer().length()
                                : 0);

                log.info(
                        "[ASSISTANT] Sources count: {}",
                        response.sources() != null
                                ? response.sources().size()
                                : 0);
            } else {
                log.warn("[ASSISTANT] Service returned null response");
            }

            log.info("[ASSISTANT] =============================");

            return response;

        } catch (Exception e) {

            log.error(
                    "[ASSISTANT] Query failed: {}",
                    e.getMessage(),
                    e);

            log.info("[ASSISTANT] =============================");

            throw e;
        }
    }

    @PostMapping(value = "/query/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> queryStream(
            @Valid @RequestBody AssistantQueryRequest request) {

        log.info("[ASSISTANT] Streaming query: {}", request.question());

        return service.askStream(request)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

}
