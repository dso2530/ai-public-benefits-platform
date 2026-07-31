package com.govtech.consent.application.usecase;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.govtech.consent.domain.model.Consent;
import com.govtech.consent.domain.model.ConsentStatus;
import com.govtech.consent.domain.port.ConsentRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentRepositoryPort repository;

    public Consent grant(
            UUID userId,
            String purpose,
            String version,
            String source) {

        Consent consent = Consent.builder()

                .id(UUID.randomUUID())

                .userId(userId)

                .purpose(purpose)

                .version(version)

                .source(source)

                .status(ConsentStatus.GRANTED)

                .grantedAt(Instant.now())

                .build();

        return repository.save(consent);

    }

    public boolean hasConsent(
            UUID userId,
            String purpose) {

        return repository
                .findByUserIdAndPurpose(
                        userId,
                        purpose)
                .map(Consent::canProcess)
                .orElse(false);

    }

}