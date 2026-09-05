package com.govtech.apply.application.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.apply.application.cerfa.CerfaGenerationService;
import com.govtech.apply.application.client.ProfileClient;
import com.govtech.apply.application.client.dto.ProfileContractDto;
import com.govtech.apply.application.dto.DocumentAnalysis;
import com.govtech.apply.application.dto.PackageDocument;
import com.govtech.apply.application.dto.PackageResult;
import com.govtech.apply.application.dto.StoredDocument;
import com.govtech.apply.application.event.ApplicationEventService;
import com.govtech.apply.domain.model.Application;
import com.govtech.apply.domain.model.ApplicationStatus;
import com.govtech.apply.infrastructure.persistence.ApplicationPackageJpaEntity;
import com.govtech.apply.infrastructure.persistence.ApplicationPackageRepository;
import com.govtech.platform.storage.service.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationPackageService {

    private final ApplicationService applicationService;
    private final ApplicationDocumentService documentService;

    private final ProfileClient profileClient;
    private final CerfaGenerationService cerfaGenerationService;
    private final PackageBuilderService packageBuilder;

    private final StorageService storageService;
    private final ApplicationPackageRepository packageRepository;


    @Transactional
    public void generatePackage(
            UUID applicationId, String subject) {

        Application application = applicationService.findDomain(
                applicationId,
                subject);

        generatePackage(
                application);
    }

    @Transactional
    public void refresh(
            UUID applicationId,
            String subject) {

        Application application = applicationService.findDomain(
                applicationId,
                subject);

        DocumentAnalysis analysis = documentService.analyze(
                application.aidCode(),
                application.subject());

        try {

            generatePackage(
                    application,
                    analysis);

        } catch (Exception e) {

            application = application.withStatus(
                    ApplicationStatus.READY_TO_COMPLETE);

            applicationService.save(application);

            throw e;
        }
    }

    private void generatePackage(
            Application application) {

        DocumentAnalysis analysis = documentService.analyze(
                application.aidCode(),
                application.subject());

        generatePackage(
                application,
                analysis);
    }

    private void generatePackage(
            Application application,
            DocumentAnalysis analysis) {

        ProfileContractDto profile = profileClient.getProfile(
                application.subject());

        List<PackageDocument> documents = downloadDocuments(
                analysis.availableDocuments());

        byte[] cerfa = cerfaGenerationService.generate(
                application,
                profile);

        PackageResult result = packageBuilder.build(
                cerfa,
                documents,
                analysis.missingDocuments());

        String objectKey = "applications/%s/application-package.zip"
                .formatted(application.applicationId());

        String uploadedObjectKey = storageService.upload(
                new ByteArrayInputStream(result.zip()),
                result.zip().length,
                "application/zip",
                "applications",
                objectKey);

        persistPackage(
                application,
                uploadedObjectKey,
                result);

        Application updated = application.withStatus(
                result.applicationStatus())
                .withMissingDocuments(
                        analysis.missingDocuments());

        applicationService.save(updated);

    }

    private List<PackageDocument> downloadDocuments(
            java.util.Map<com.govtech.shared.model.DocumentType, StoredDocument> documents) {

        List<PackageDocument> result = new ArrayList<>();

        for (StoredDocument document : documents.values()) {

            try (InputStream input = storageService.download(
                    "documents-user",
                    document.objectKey())) {

                byte[] content = input.readAllBytes();

                result.add(
                        new PackageDocument(
                                document.fileName(),
                                content));

            } catch (IOException e) {

                throw new UncheckedIOException(
                        "Unable to download document "
                                + document.objectKey(),
                        e);
            }
        }

        return result;
    }

    private void persistPackage(
            Application application,
            String objectKey,
            PackageResult result) {

        ApplicationPackageJpaEntity pkg = packageRepository
                .findByApplicationId(
                        application.applicationId())
                .orElseGet(
                        ApplicationPackageJpaEntity::new);

        pkg.setApplicationId(
                application.applicationId());

        pkg.setObjectKey(objectKey);

        pkg.setStatus(
                result.status());

        pkg.setGeneratedAt(
                Instant.now());

        packageRepository.save(pkg);
    }
}