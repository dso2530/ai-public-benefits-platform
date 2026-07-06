package com.govtech.extraction.infrastructure.llm.client;

import com.govtech.extraction.infrastructure.llm.dto.ChatRequest;
import com.govtech.extraction.infrastructure.llm.dto.ChatResponse;
import com.govtech.extraction.infrastructure.llm.dto.Message;
import com.govtech.extraction.infrastructure.llm.exception.ExtractionException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OllamaClient {

    private final RestClient llmRestClient;

    @Value("${llm.model}")
    private String model;

    public String extract(String prompt) {

        ChatRequest request = new ChatRequest(
                model,
                List.of(
                        new Message("system",
                                "You extract structured information and return only JSON."),
                        new Message("user", prompt)),
                false, false);

        try {

            log.info("Calling Ollama...");
            ChatResponse response = llmRestClient.post()
                    .uri("/api/chat")
                    .body(request)
                    .retrieve()
                    .body(ChatResponse.class);

            if (response == null || response.message() == null) {
                throw new ExtractionException("Empty response returned by the LLM");
            }

            log.info("LLM response received");

            return response.message().content();

        } catch (Exception e) {
            throw new ExtractionException("Failed to extract structured data using the LLM", e);
        }
    }
}