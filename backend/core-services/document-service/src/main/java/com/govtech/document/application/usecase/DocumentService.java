package com.govtech.document.application.usecase;

import com.govtech.document.api.dto.DocumentContractDto;
import com.govtech.document.api.dto.DocumentDto;
import com.govtech.document.api.dto.DocumentSummaryDto;
import com.govtech.document.application.dto.DownloadedDocument;
import com.govtech.document.application.event.DocumentEventFactory;
import com.govtech.document.application.mapper.DocumentMapper;
import com.govtech.document.application.service.DocumentOutboxService;

import com.govtech.document.infrastructure.persistence.DocumentJpaEntity;
import com.govtech.document.infrastructure.persistence.DocumentJpaMapper;
import com.govtech.document.infrastructure.persistence.DocumentJpaRepository;
import com.govtech.platform.storage.service.StorageService;
import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentStatus;
import com.govtech.shared.model.DocumentType;
import com.govtech.shared.model.SecurityStatus;

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

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentService
                implements DocumentServiceUsecase, InternalDocumentServiceUseCase {

        private static final String USER_QUARANTINE_BUCKET = "documents-user-quarantine";

        private static final String USER_DOCUMENT_BUCKET = "documents-user";

        private final StorageService storageService;

        private final DocumentJpaRepository repository;

        private final DocumentMapper mapper;

        private final DocumentOutboxService outboxService;

        private final DocumentEventFactory documentEventFactory;

        private final DocumentJpaMapper documentJpaMapper;

        // -------------------------------------------------------------------------
        // READ
        // -------------------------------------------------------------------------

        @Override
        @Transactional(readOnly = true)
        public List<DocumentDto> getDocuments(
                        final @NonNull String subject) {

                return repository
                                .findBySubjectOrderByUploadedAtDesc(subject)
                                .stream()
                                .map(doc -> new DocumentDto(
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
        public List<DocumentContractDto> findBySubject(
                        final @NonNull String subject) {

                return repository
                                .findBySubjectOrderByUploadedAtDesc(subject)
                                .stream()
                                .map(doc -> DocumentContractDto.builder()
                                                .documentId(doc.getId())
                                                .subject(doc.getSubject())
                                                .name(doc.getName())
                                                .documentType(doc.getDocumentType())
                                                .status(doc.getStatus())
                                                .fileName(doc.getFileName())
                                                .contentType(doc.getContentType())
                                                .objectKey(doc.getObjectKey())
                                                .fileSize(doc.getFileSize())
                                                .uploadedAt(doc.getUploadedAt())
                                                .build())
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public DocumentSummaryDto getSummary(
                        final @NonNull String subject) {

                long total = repository.countBySubject(subject);

                long validated = repository.countBySubjectAndStatus(
                                subject,
                                "VALIDATED");

                return new DocumentSummaryDto(
                                (int) total,
                                (int) validated,
                                (int) (total - validated));
        }

        // -------------------------------------------------------------------------
        // UPLOAD
        // -------------------------------------------------------------------------

        @Override
        public DocumentDto upload(
                        @NonNull String subject,
                        @NonNull MultipartFile file,
                        @NonNull String type,
                        UUID applicationId)
                        throws IOException {

                String objectKey = "citizens/%s/%s-%s".formatted(
                                subject,
                                UUID.randomUUID(),
                                file.getOriginalFilename());

                /*
                 * 1. Upload dans le bucket de quarantaine.
                 */
                storageService.upload(
                                file.getInputStream(),
                                file.getSize(),
                                file.getContentType(),
                                USER_QUARANTINE_BUCKET,
                                objectKey);

                /*
                 * 2. Calcul du hash.
                 */
                String sha256 = DigestUtils.sha256Hex(
                                file.getBytes());

                DocumentType documentType = DocumentType.valueOf(type);

                /*
                 * 3. Construction de l'entité métier/persistence.
                 */
                DocumentJpaEntity document = DocumentJpaEntity.builder()

                                .subject(subject)

                                .applicationId(applicationId)

                                .name(
                                                documentType.getName())

                                .documentType(
                                                documentType)

                                .status(
                                                DocumentStatus.UPLOADED.name())

                                .securityStatus(
                                                SecurityStatus.PENDING)

                                .fileName(
                                                file.getOriginalFilename())

                                .sha256(
                                                sha256)

                                .bucket(
                                                USER_QUARANTINE_BUCKET)

                                .objectKey(
                                                objectKey)

                                .fileSize(
                                                file.getSize())

                                .contentType(
                                                file.getContentType())

                                .uploadedAt(
                                                Instant.now())

                                .origin(
                                                DocumentOrigin.USER_UPLOAD)

                                .source(
                                                "USER")

                                .build();

                /*
                 * 4. Transaction DB :
                 *
                 * INSERT document
                 * INSERT outbox_event
                 *
                 * Les deux sont dans la même transaction.
                 */
                DocumentJpaEntity saved = repository.save(document);

                /*
                 * 5. Création de l'événement Outbox.
                 *
                 * DocumentEventFactory construit le contrat Avro.
                 * DocumentOutboxService le sérialise et le stocke
                 * dans outbox_event.
                 */
                outboxService.publish(
                                documentJpaMapper.toDomain(saved),
                                documentEventFactory::buildScanRequested,
                                "DocumentScanRequested",
                                "document.scan.requested");

                /*
                 * 6. Retour API.
                 */
                return mapper.toDto(saved);
        }

        // -------------------------------------------------------------------------
        // DELETE
        // -------------------------------------------------------------------------

        @Override
        public void delete(
                        @NonNull Long id,
                        @NonNull String subject)
                        throws AccessDeniedException {

                DocumentJpaEntity document = repository.findById(id)
                                .orElseThrow(
                                                () -> new EntityNotFoundException(
                                                                "Document not found: " + id));

                if (!document.getSubject().equals(subject)) {
                        throw new AccessDeniedException(
                                        "Document does not belong to user");
                }

                storageService.delete(
                                USER_DOCUMENT_BUCKET,
                                document.getObjectKey());

                repository.delete(document);
        }

        // -------------------------------------------------------------------------
        // DOWNLOAD
        // -------------------------------------------------------------------------

        @Override
        @Transactional(readOnly = true)
        public DownloadedDocument download(
                        @NonNull Long id,
                        @NonNull String subject)
                        throws AccessDeniedException {

                DocumentJpaEntity document = repository.findById(id)
                                .orElseThrow(
                                                () -> new EntityNotFoundException(
                                                                "Document not found: " + id));

                if (!document.getSubject().equals(subject)) {
                        throw new AccessDeniedException(
                                        "Document does not belong to user");
                }

                try (InputStream inputStream = storageService.download(
                                USER_DOCUMENT_BUCKET,
                                document.getObjectKey())) {

                        byte[] content = inputStream.readAllBytes();

                        return new DownloadedDocument(
                                        document.getFileName(),
                                        document.getContentType(),
                                        content);

                } catch (IOException e) {
                        throw new UncheckedIOException(e);
                }
        }
}