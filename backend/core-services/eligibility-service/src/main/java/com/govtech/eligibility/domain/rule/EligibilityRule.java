package com.govtech.eligibility.domain.rule;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.govtech.eligibility.application.dto.ProfileContractDto;
import com.govtech.eligibility.domain.model.Eligibility;

public interface EligibilityRule {

    Optional<Eligibility> evaluate(
            ProfileContractDto profile,
            UUID calculationId,
            Instant calculatedAt);

}