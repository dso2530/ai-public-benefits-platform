package com.govtech.bff.clients;

import com.govtech.bff.dashboard.dto.DocumentSummaryDto;
import com.govtech.bff.documents.dto.DocumentDto;
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
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class DocumentClient {

  private final RestClient restClient;

  @Value("${services.document.url}")
  private String documentUrl;

  public List<DocumentDto> getDocuments() {

    return restClient
        .get()
        .uri(documentUrl + "/api/documents")
        .retrieve()
        .body(new ParameterizedTypeReference<List<DocumentDto>>() {});
  }

  public DocumentSummaryDto getSummary() {

    return restClient
        .get()
        .uri(documentUrl + "/api/documents/summary")
        .retrieve()
        .body(DocumentSummaryDto.class);
  }

  public DocumentDto upload(MultipartFile file, String documentType) throws IOException {

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    body.add(
        "file",
        new ByteArrayResource(file.getBytes()) {
          @Override
          public String getFilename() {
            return file.getOriginalFilename();
          }
        });

    body.add("documentType", documentType);

    return restClient
        .post()
        .uri(documentUrl + "/api/documents")
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(body)
        .retrieve()
        .body(DocumentDto.class);
  }

  public DocumentDto uploadDocumentsMissing(
      UUID applicationId, MultipartFile file, String documentType) throws IOException {

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    body.add(
        "file",
        new ByteArrayResource(file.getBytes()) {
          @Override
          public String getFilename() {
            return file.getOriginalFilename();
          }
        });

    body.add("documentType", documentType);

    body.add("applicationId", applicationId.toString());

    return restClient
        .post()
        .uri(documentUrl + "/api/documents")
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(body)
        .retrieve()
        .body(DocumentDto.class);
  }

  public void delete(Long id) {
    restClient.delete().uri(documentUrl + "/api/documents/{id}", id).retrieve().toBodilessEntity();
  }

  public byte[] download(Long id) {
    return restClient
        .get()
        .uri(documentUrl + "/api/documents/{id}/download", id)
        .retrieve()
        .body(byte[].class);
  }
}
