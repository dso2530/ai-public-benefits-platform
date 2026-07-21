package com.govtech.application.infrastructure.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.application.domain.model.StoredDocument;
import com.govtech.application.service.client.DocumentClient;
import com.govtech.application.service.client.dto.DocumentContractDto;

@Component
public class DocumentClientImpl implements DocumentClient {

    private final RestClient restClient;

    public DocumentClientImpl(
            @Value("${clients.document.base-url}") String baseUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    @Override
    public List<StoredDocument> getDocuments(String subject) {

        List<DocumentContractDto> documents = restClient.get()
                .uri("/internal/documents/{subject}", subject)
                .retrieve()
                .body(new ParameterizedTypeReference<List<DocumentContractDto>>() {
                });

        return documents.stream()
                .map(this::toStoredDocument)
                .toList();
    }

    private StoredDocument toStoredDocument(DocumentContractDto dto) {
        return new StoredDocument(
                dto.documentId(),
                dto.name(),
                dto.documentType(),
                dto.status(),
                dto.fileName(),
                dto.contentType(),
                dto.bucket(),
                dto.objectKey(),
                dto.fileSize());
    }
}