package com.govtech.citizen.clients;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;

import com.govtech.citizen.dashboard.dto.DocumentSummaryDto;
import com.govtech.citizen.documents.dto.DocumentDto;
import com.govtech.platform.web.error.ExternalServiceUnavailableException;
import com.govtech.platform.web.rest.RestClientErrorHandler;

@Component
@RequiredArgsConstructor
public class DocumentClient {

    private static final String SERVICE_NAME = "document-service";

    private final RestClient restClient;

    @Value("${services.document.url}")
    private String documentUrl;

    public List<DocumentDto> getDocuments() {

        try {

            return restClient
                    .get()
                    .uri(documentUrl + "/api/documents")
                    .retrieve()
                    .body(
                            new ParameterizedTypeReference<List<DocumentDto>>() {
                            }
                    );

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME
            );

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    public DocumentSummaryDto getSummary() {

        try {

            return restClient
                    .get()
                    .uri(documentUrl + "/api/documents/summary")
                    .retrieve()
                    .body(DocumentSummaryDto.class);

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME
            );

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    public DocumentDto upload(
            MultipartFile file,
            String documentType) throws IOException {

        MultiValueMap<String, Object> body =
                buildMultipartBody(
                        file,
                        documentType,
                        null
                );

        try {

            return restClient
                    .post()
                    .uri(documentUrl + "/api/documents")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(DocumentDto.class);

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME
            );

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    public DocumentDto uploadDocumentsMissing(
            UUID applicationId,
            MultipartFile file,
            String documentType) throws IOException {

        MultiValueMap<String, Object> body =
                buildMultipartBody(
                        file,
                        documentType,
                        applicationId
                );

        try {

            return restClient
                    .post()
                    .uri(documentUrl + "/api/documents")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(DocumentDto.class);

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME
            );

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    public void delete(Long id) {

        try {

            restClient
                    .delete()
                    .uri(
                            documentUrl + "/api/documents/{id}",
                            id
                    )
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME
            );

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    public byte[] download(Long id) {

        try {

            return restClient
                    .get()
                    .uri(
                            documentUrl
                                    + "/api/documents/{id}/download",
                            id
                    )
                    .retrieve()
                    .body(byte[].class);

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME
            );

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    private MultiValueMap<String, Object> buildMultipartBody(
            MultipartFile file,
            String documentType,
            UUID applicationId) throws IOException {

        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();

        body.add(
                "file",
                new ByteArrayResource(file.getBytes()) {

                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                }
        );

        body.add(
                "documentType",
                documentType
        );

        if (applicationId != null) {
            body.add(
                    "applicationId",
                    applicationId.toString()
            );
        }

        return body;
    }

    private ExternalServiceUnavailableException serviceUnavailable(
            ResourceAccessException ex) {

        return new ExternalServiceUnavailableException(
                "Le service " + SERVICE_NAME
                        + " est indisponible.",
                ex
        );
    }
}
