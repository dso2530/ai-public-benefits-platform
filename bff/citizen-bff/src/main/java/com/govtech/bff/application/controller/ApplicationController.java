package com.govtech.bff.application.controller;

import com.govtech.bff.application.dto.ApplicationDto;
import com.govtech.bff.application.service.ApplicationService;
import com.govtech.bff.clients.DocumentClient;
import com.govtech.bff.documents.dto.DocumentDto;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

  private final ApplicationService service;

  private final DocumentClient documentClient;

  @GetMapping
  public List<ApplicationDto> findAllApplications() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ApplicationDto findById(@PathVariable UUID id) {
    return service.findById(id);
  }

  @PostMapping("/{id}/submit")
  public void submit(@PathVariable UUID id) {
    service.submit(id);
  }

  @GetMapping("/{id}/document")
  public ResponseEntity<byte[]> download(@PathVariable UUID id) {

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"application-package.zip\"")
        .body(service.downloadPackage(id));
  }

  @PostMapping("/{id}/documents")
  public DocumentDto upload(
      @PathVariable UUID id, @RequestPart MultipartFile file, @RequestPart String documentType)
      throws IOException {

    return documentClient.uploadDocumentsMissing(id, file, documentType);
  }
}
