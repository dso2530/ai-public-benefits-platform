package com.govtech.apply.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.apply.domain.model.Application;
import com.govtech.apply.infrastructure.persistence.ApplicationJpaEntity;
import com.govtech.apply.infrastructure.persistence.ApplicationJpaMapper;
import com.govtech.apply.infrastructure.persistence.ApplicationJpaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationJpaRepository repository;
    private final ApplicationJpaMapper mapper;

    @Transactional
    public Application create(
            Application application) {

        ApplicationJpaEntity entity = repository.save(
                mapper.toJpaEntity(application));

        return mapper.toDomain(entity);
    }

    @Transactional(readOnly = true)
    public Application findDomain(
            UUID applicationId,
            String subject) {

        return repository
                .findByApplicationIdAndSubject(
                        applicationId,
                        subject)
                .map(mapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Application not found"));
    }

    @Transactional
    public Application save(
            Application application) {

        return repository
                .findByApplicationIdAndSubject(
                        application.applicationId(),
                        application.subject())
                .map(entity -> {
                    ApplicationJpaEntity updated = mapper.update(
                            entity,
                            application);

                    return mapper.toDomain(
                            repository.save(updated));
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        "Application not found"));
    }
}