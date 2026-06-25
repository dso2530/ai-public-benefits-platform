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
import java.util.Objects;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
      final @NonNull String subject, final @NonNull MultipartFile file, final @NonNull String type)
      throws IOException {

    // Nom original uniquement pour l'affichage
    String originalFileName =
        StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

    // Extraction sécurisée de l'extension
    String extension = "";

    int lastDotIndex = originalFileName.lastIndexOf('.');
    if (lastDotIndex > 0) {
      extension = originalFileName.substring(lastDotIndex);
    }

    // Nom physique stocké sur disque
    String storedFileName = UUID.randomUUID() + extension;

    Path uploadDir = Path.of(storagePath).toAbsolutePath().normalize();

    Files.createDirectories(uploadDir);

    Path target = uploadDir.resolve(storedFileName).normalize();

    // Protection contre Path Traversal
    if (!target.startsWith(uploadDir)) {
      throw new SecurityException("Invalid file path");
    }

    Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

    DocumentJpaEntity entity = new DocumentJpaEntity();

    entity.setSubject(subject);

    // Nom original visible par l'utilisateur
    entity.setName(originalFileName);

    // Nom physique sur disque
    entity.setFileName(storedFileName);

    entity.setDocumentType(DocumentType.valueOf(type));

    entity.setUploadedAt(Instant.now());

    DocumentJpaEntity saved = repository.save(entity);

    return mapToDto(saved);
  }

  private DocumentDto mapToDto(DocumentJpaEntity entity) {

    return DocumentDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .fileName(entity.getFileName())
        .documentType(entity.getDocumentType().name())
        .uploadedAt(entity.getUploadedAt())
        .build();
  }

  @Override
  public void delete(final @NonNull Long id, final @NonNull String subject) throws IOException {
    DocumentJpaEntity document =
        repository
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
    DocumentJpaEntity document =
        repository
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
