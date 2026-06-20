package com.govtech.bff.clients;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.bff.dashboard.dto.DocumentDto;
import com.govtech.bff.dashboard.dto.DocumentSummaryDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DocumentClient {

    private final RestClient restClient;

    @Value("${services.document.url}")
    private String documentUrl;

    public List<DocumentDto> getDocuments() {

        return restClient.get()
                .uri(documentUrl + "/api/documents")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public DocumentSummaryDto getSummary() {

        return restClient.get()
                .uri(documentUrl + "/api/documents/summary")
                .retrieve()
                .body(DocumentSummaryDto.class);
    }
}