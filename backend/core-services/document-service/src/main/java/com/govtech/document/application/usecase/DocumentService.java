package com.govtech.document.application.usecase;

import com.govtech.document.application.dto.DocumentDto;
import com.govtech.document.application.dto.DocumentSummaryDto;
import com.govtech.document.application.dto.DocumentType;
import com.govtech.document.application.dto.DownloadedDocument;
import com.govtech.document.application.mapper.DocumentMapper;
import com.govtech.document.infrastructure.persistence.DocumentJpaEntity;
import com.govtech.document.infrastructure.persistence.DocumentJpaRepository;
import com.govtech.platform.storage.service.StorageService;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentService implements DocumentServiceUsecase {

  private final StorageService storageService;
  private final DocumentJpaRepository repository;
  private final DocumentMapper mapper;
  private final DocumentEventService documentEventService;

  @Override
  @Transactional(readOnly = true)
  public List<DocumentDto> getDocuments(final @NonNull String subject) {

    return repository.findBySubjectOrderByUploadedAtDesc(subject).stream()
        .map(
            doc ->
                new DocumentDto(
                    doc.getId(),
                    doc.getName(),
                    doc.getDocumentType().name(),
                    doc.getStatus(),
                    doc.getFileName(),
                    doc.getFileSize(),
                    doc.getUploadedAt()))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public DocumentSummaryDto getSummary(final @NonNull String subject) {

    long total = repository.countBySubject(subject);

    long validated = repository.countBySubjectAndStatus(subject, "VALIDATED");

    return new DocumentSummaryDto((int) total, (int) validated, (int) (total - validated));
  }

  @Override
  public DocumentDto upload(
      @NonNull String subject, @NonNull MultipartFile file, @NonNull String type)
      throws IOException {

    String objectKey =
        "citizens/%s/%s-%s".formatted(subject, UUID.randomUUID(), file.getOriginalFilename());

    storageService.upload(file.getInputStream(), file.getSize(), file.getContentType(), objectKey);

    DocumentJpaEntity document =
        DocumentJpaEntity.builder()
            .subject(subject)
            .name(DocumentType.valueOf(type).getName())
            .documentType(DocumentType.valueOf(type))
            .status("UPLOADED")
            .fileName(file.getOriginalFilename())
            .objectKey(objectKey) // clé MinIO
            .fileSize(file.getSize())
            .contentType(file.getContentType())
            .uploadedAt(Instant.now())
            .build();

    DocumentJpaEntity saved = repository.save(document);

    documentEventService.publishUploaded(saved);

    return mapper.toDto(saved);
  }

  @Override
  public void delete(@NonNull Long id, @NonNull String subject) throws AccessDeniedException {

    DocumentJpaEntity document =
        repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Document not found: " + id));

    if (!document.getSubject().equals(subject)) {
      throw new AccessDeniedException("Document does not belong to user");
    }

    storageService.delete(document.getObjectKey());

    repository.delete(document);
  }

  @Override
  @Transactional(readOnly = true)
  public DownloadedDocument download(@NonNull Long id, @NonNull String subject)
      throws AccessDeniedException {

    DocumentJpaEntity document =
        repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Document not found: " + id));

    if (!document.getSubject().equals(subject)) {
      throw new AccessDeniedException("Document does not belong to user");
    }

    try (InputStream inputStream = storageService.download(document.getObjectKey())) {

      byte[] content = inputStream.readAllBytes();

      return new DownloadedDocument(document.getFileName(), document.getContentType(), content);

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
