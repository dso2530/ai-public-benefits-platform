package com.govtech.document.application.usecase;

import com.govtech.document.application.dto.DocumentDto;
import com.govtech.document.application.dto.DocumentSummaryDto;
import com.govtech.document.application.dto.DocumentType;
import com.govtech.document.application.dto.DownloadedDocument;
import com.govtech.document.application.mapper.DocumentMapper;
import com.govtech.document.infrastructure.persistence.DocumentJpaEntity;
import com.govtech.document.infrastructure.persistence.DocumentJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentService implements DocumentServiceUsecase {

  @Value("${documents.storage-path:/data/documents}")
  private String storagePath;

  private final DocumentJpaRepository repository;
  private final DocumentMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public List<DocumentDto> getDocuments(final @NonNull String subject) {

    return repository.findBySubjectOrderByUploadedAtDesc(subject).stream()
        .map(
            doc -> new DocumentDto(
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
      final @NonNull String subject, final @NonNull MultipartFile file, final @NonNull String type)
      throws IOException {

    String storedFileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

    Path uploadDir = Path.of(storagePath);

    Files.createDirectories(uploadDir);

    Path target = uploadDir.resolve(storedFileName).normalize();

    if (!target.startsWith(uploadDir)) {
      throw new SecurityException("Invalid file path");
    }

    Files.copy(
        file.getInputStream(),
        target,
        StandardCopyOption.REPLACE_EXISTING);

    DocumentJpaEntity document = DocumentJpaEntity.builder()
        .subject(subject)
        .name(DocumentType.valueOf(type).getName())
        .documentType(DocumentType.valueOf(type))
        .status("UPLOADED")
        .fileName(file.getOriginalFilename())
        .filePath(target.toString())
        .fileSize(file.getSize())
        .contentType(file.getContentType())
        .uploadedAt(Instant.now())
        .build();

    return mapper.toDto(
        repository.save(document));
  }

  @Override
  public void delete(@NonNull Long id, @NonNull String subject) throws IOException {
    DocumentJpaEntity document = repository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Document not found: " + id));

    if (!document.getSubject().equals(subject)) {
      throw new AccessDeniedException("Document does not belong to user");
    }

    Files.deleteIfExists(Path.of(document.getFilePath()));

    repository.delete(document);
  }

  @Override
  @Transactional(readOnly = true)
  public DownloadedDocument download(final @NonNull Long id, final @NonNull String subject)
      throws AccessDeniedException {
    DocumentJpaEntity document = repository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Document not found: " + id));

    if (!document.getSubject().equals(subject)) {
      throw new AccessDeniedException("Document does not belong to user");
    }

    try {

      byte[] content = Files.readAllBytes(Path.of(document.getFilePath()));

      return new DownloadedDocument(document.getFileName(), document.getContentType(), content);

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
