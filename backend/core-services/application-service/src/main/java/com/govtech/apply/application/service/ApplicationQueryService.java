package com.govtech.apply.application.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.apply.api.dto.ApplicationDto;
import com.govtech.apply.api.mapper.ApplicationDtoMapper;
import com.govtech.apply.infrastructure.persistence.ApplicationJpaEntity;
import com.govtech.apply.infrastructure.persistence.ApplicationJpaMapper;
import com.govtech.apply.infrastructure.persistence.ApplicationJpaRepository;
import com.govtech.apply.infrastructure.persistence.ApplicationPackageJpaEntity;
import com.govtech.apply.infrastructure.persistence.ApplicationPackageRepository;
import com.govtech.platform.storage.dto.DownloadedDocument;
import com.govtech.platform.storage.service.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationQueryService {

    private static final String APPLICATION_BUCKET = "applications";

    private final ApplicationJpaRepository applicationRepository;
    private final ApplicationPackageRepository packageRepository;
    private final ApplicationDtoMapper mapper;
    private final ApplicationJpaMapper entityMapper;

    private final StorageService storageService;

    public Optional<ApplicationDto> findById(
            UUID applicationId,
            String subject) {

        return applicationRepository
                .findByApplicationIdAndSubject(
                        applicationId,
                        subject)
                .map(entityMapper::toDomain)
                .map(mapper::toDto);
    }

    public List<ApplicationDto> findBySubject(
            String subject) {

        return applicationRepository
                .findBySubjectOrderByCreatedAtDesc(subject)
                .stream()
                .map(entityMapper::toDomain)
                .map(mapper::toDto)
                .toList();
    }

    public DownloadedDocument downloadPackage(
            UUID applicationId,
            String subject)
            throws AccessDeniedException {

        ApplicationJpaEntity application = applicationRepository
                .findByApplicationIdAndSubject(applicationId, subject)
                .orElseThrow(
                        () -> new AccessDeniedException(
                                "Application not found"));

        ApplicationPackageJpaEntity applicationPackage = packageRepository
                .findByApplicationId(application.getApplicationId())
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Application package not found"));

        if (applicationPackage.getObjectKey() == null) {
            throw new IllegalStateException(
                    "Application package has no object key");
        }

        try (InputStream stream = storageService.download(
                APPLICATION_BUCKET,
                applicationPackage.getObjectKey())) {

            return new DownloadedDocument(
                    applicationPackage.getObjectKey(),
                    "application/zip",
                    stream.readAllBytes());

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to download application package",
                    e);
        }
    }
}