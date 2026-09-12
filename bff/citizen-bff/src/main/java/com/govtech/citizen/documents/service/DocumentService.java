package com.govtech.citizen.documents.service;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.govtech.citizen.clients.DocumentClient;
import com.govtech.citizen.documents.dto.DocumentDto;

@Service
@RequiredArgsConstructor
public class DocumentService {

  private final DocumentClient documentClient;

  public List<DocumentDto> documents() {
    return documentClient.getDocuments();
  }

  public DocumentDto upload(MultipartFile file, String documentType) throws IOException {

    return documentClient.upload(file, documentType);
  }

  public void delete(Long id) {
    documentClient.delete(id);
  }

  public byte[] download(Long id) {
    return documentClient.download(id);
  }
}
