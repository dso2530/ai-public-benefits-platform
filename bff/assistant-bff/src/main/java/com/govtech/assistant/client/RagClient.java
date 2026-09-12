package com.govtech.assistant.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.assistant.client.dto.RagQueryRequest;
import com.govtech.assistant.client.dto.RagResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RagClient {

    private final RestClient restClient;

    @Value("${services.territorial-rag.url}")
    private String ragUrl;

    public RagResponse query(
            RagQueryRequest request) {

        return restClient.post()
                .uri(ragUrl + "/api/rag/query")
                .body(request)
                .retrieve()
                .body(RagResponse.class);

    }
}