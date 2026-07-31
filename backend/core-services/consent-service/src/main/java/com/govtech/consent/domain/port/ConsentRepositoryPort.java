package com.govtech.consent.domain.port;

import java.util.Optional;
import java.util.UUID;

import com.govtech.consent.domain.model.Consent;

public interface ConsentRepositoryPort {

    Consent save(Consent consent);

    Optional<Consent> findByUserIdAndPurpose(
            UUID userId,
            String purpose);

}