package com.govtech.application.service.usecase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.application.api.dto.ApplicationDto;
import com.govtech.application.api.mapper.ApplicationDtoMapper;
import com.govtech.application.domain.model.Application;
import com.govtech.application.domain.model.ApplicationStatus;
import com.govtech.application.domain.model.StoredDocument;
import com.govtech.application.infrastructure.persistence.ApplicationJpaEntity;
import com.govtech.application.infrastructure.persistence.ApplicationJpaMapper;
import com.govtech.application.infrastructure.persistence.ApplicationJpaRepository;
import com.govtech.application.infrastructure.persistence.ApplicationPackageJpaEntity;
import com.govtech.application.infrastructure.persistence.ApplicationPackageRepository;
import com.govtech.application.service.cerfa.CerfaGenerationService;
import com.govtech.application.service.client.DocumentClient;
import com.govtech.application.service.client.ProfileClient;
import com.govtech.application.service.client.dto.ProfileContractDto;
import com.govtech.application.service.dto.PackageDocument;
import com.govtech.application.service.dto.PackageResult;
import com.govtech.application.service.dto.RequiredDocument;
import com.govtech.platform.storage.dto.DownloadedDocument;
import com.govtech.platform.storage.service.StorageService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import com.govtech.application.domain.model.DocumentType;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ApplicationService {

        private final ApplicationJpaRepository repository;
        private final ApplicationJpaMapper applicationMapper;
        private final ApplicationDtoMapper applicationDtoMapper;

        private final CerfaGenerationService cerfaGenerationService;
        private final PackageBuilder packageBuilder;

        private final RulesService rulesService;
        private final ProfileClient profileClient;
        private final DocumentClient documentClient;

        private final StorageService storageService;
        private final ApplicationPackageRepository packageRepository;
        private final ApplicationEventService eventService;

        private record DocumentAnalysis(
                        Map<DocumentType, StoredDocument> availableDocuments,
                        List<DocumentType> missingDocuments) {
        }

        private DocumentAnalysis analyzeDocuments(
                        String aidCode,
                        String subject) {

                List<RequiredDocument> requiredDocuments = rulesService.requiredDocuments(aidCode);

                List<StoredDocument> storedDocuments = documentClient.getDocuments(subject);

                Map<DocumentType, StoredDocument> availableDocuments = storedDocuments.stream()
                                .collect(Collectors.toMap(
                                                StoredDocument::documentType,
                                                Function.identity(),
                                                (existing, replacement) -> replacement));

                List<DocumentType> missingDocuments = requiredDocuments.stream()
                                .map(RequiredDocument::type)
                                .filter(type -> !availableDocuments.containsKey(type))
                                .toList();

                return new DocumentAnalysis(
                                availableDocuments,
                                missingDocuments);
        }

        @Transactional
        public void refreshApplication(UUID applicationId, String subject) {

                ApplicationJpaEntity application = repository
                                .findByApplicationIdAndSubject(applicationId, subject)
                                .orElseThrow();

                DocumentAnalysis analysis = analyzeDocuments(
                                application.getAidCode(),
                                application.getSubject());

                log.info("Documents manquants : {}", analysis.missingDocuments());

                generatePackage(application, analysis);
        }

        @Transactional(readOnly = true)
        public Optional<ApplicationDto> findById(UUID id, String subject) {

                return repository.findByApplicationIdAndSubject(id, subject)
                                .map(applicationMapper::toDomain)
                                .map(applicationDtoMapper::toDto);
        }

        @Transactional(readOnly = true)
        public DownloadedDocument downloadPackage(UUID id, String subject)
                        throws AccessDeniedException {

                ApplicationJpaEntity application = repository
                                .findByApplicationIdAndSubject(id, subject)
                                .orElseThrow(() -> new EntityNotFoundException("Application not found"));

                log.info("Downloading package for applicationId={}", application.getApplicationId());

                ApplicationPackageJpaEntity pkg = packageRepository
                                .findByApplicationId(application.getApplicationId())
                                .orElseThrow(() -> new EntityNotFoundException("Application package not found"));

                try (InputStream inputStream = storageService.download("applications", pkg.getObjectKey())) {

                        return new DownloadedDocument(
                                        application.getAidCode() + ".zip",
                                        "application/zip",
                                        inputStream.readAllBytes());

                } catch (IOException e) {
                        throw new UncheckedIOException(e);
                }
        }

        @Transactional(readOnly = true)
        public List<ApplicationDto> findBySubject(String subject) {

                return repository.findBySubjectOrderByCreatedAtDesc(subject)
                                .stream()
                                .collect(Collectors.toMap(
                                                ApplicationJpaEntity::getAidCode,
                                                Function.identity(),
                                                (first, second) -> first,
                                                LinkedHashMap::new))
                                .values()
                                .stream()
                                .map(applicationMapper::toDomain)
                                .map(applicationDtoMapper::toDto)
                                .toList();
        }

        public Application create(Application application) {

                ApplicationJpaEntity entity = repository.save(applicationMapper.toJpaEntity(application));

                try {
                        DocumentAnalysis analysis = analyzeDocuments(
                                        entity.getAidCode(),
                                        entity.getSubject());
                        generatePackage(entity, analysis);
                } catch (Exception e) {
                        log.error("Unable to generate package for application {}", entity.getApplicationId(), e);

                        entity.setStatus(ApplicationStatus.READY_TO_COMPLETE);
                        repository.save(entity);
                }

                return applicationMapper.toDomain(entity);
        }

        public void submit(UUID id, String subject) {

                ApplicationJpaEntity application = repository
                                .findByApplicationIdAndSubject(id, subject)
                                .orElseThrow();

                if (application.getStatus() != ApplicationStatus.READY_TO_SUBMIT) {
                        throw new IllegalStateException("Application is not ready to submit");
                }

                application.setStatus(ApplicationStatus.SUBMITTED);

                repository.save(application);

                eventService.publish(applicationMapper.toDomain(application));
        }

        private void generatePackage(
                        ApplicationJpaEntity application,
                        DocumentAnalysis analysis) {

                // Profil
                ProfileContractDto profile = profileClient.getProfile(application.getSubject());

                List<PackageDocument> packageDocuments = new ArrayList<>();

                for (StoredDocument document : analysis.availableDocuments().values()) {

                        try (InputStream input = storageService.download("documents", document.objectKey())) {

                                packageDocuments.add(
                                                new PackageDocument(
                                                                document.fileName(),
                                                                input.readAllBytes()));

                        } catch (IOException e) {
                                throw new UncheckedIOException(
                                                "Unable to download document " + document.objectKey(),
                                                e);
                        }
                }

                // Génération CERFA
                byte[] cerfa = cerfaGenerationService.generate(
                                application,
                                profile);

                // Construction ZIP
                PackageResult result = packageBuilder.build(
                                cerfa,
                                packageDocuments,
                                analysis.missingDocuments());

                // Upload
                String objectKey = storageService.upload(
                                new ByteArrayInputStream(result.zip()),
                                result.zip().length,
                                "application/zip",
                                "applications",
                                "applications/%s/application-package.zip"
                                                .formatted(application.getApplicationId()));

                // Persistance package
                ApplicationPackageJpaEntity pkg = packageRepository
                                .findByApplicationId(application.getApplicationId())
                                .orElseGet(ApplicationPackageJpaEntity::new);

                pkg.setApplicationId(application.getApplicationId());
                pkg.setObjectKey(objectKey);
                pkg.setStatus(result.status());
                pkg.setGeneratedAt(Instant.now());

                packageRepository.save(pkg);

                // Mise à jour application
                application.setStatus(result.applicationStatus());
                application.setMissingDocuments(analysis.missingDocuments());

                repository.save(application);

                // Publication
                eventService.publish(
                                applicationMapper.toDomain(application));
        }
}