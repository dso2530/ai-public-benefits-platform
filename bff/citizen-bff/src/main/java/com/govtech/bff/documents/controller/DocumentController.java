package com.govtech.bff.documents.controller;

import com.govtech.bff.documents.dto.DocumentDto;
import com.govtech.bff.documents.service.DocumentService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

  private final DocumentService documentService;

  @GetMapping
  public List<DocumentDto> documents() {
    return documentService.documents();
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public DocumentDto upload(@RequestParam MultipartFile file, @RequestParam String documentType)
      throws IOException {

    return documentService.upload(file, documentType);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    documentService.delete(id);
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<byte[]> download(@PathVariable("id") Long id) {

    byte[] content = documentService.download(id);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(content);
  }
}
