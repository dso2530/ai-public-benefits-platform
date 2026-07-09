package com.govtech.eligibility.domain.engine;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.eligibility.application.dto.ProfileContractDto;
import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.eligibility.domain.rule.EligibilityRule;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EligibilityEngine {

    private final List<EligibilityRule> rules;

    public List<Eligibility> evaluate(
            ProfileContractDto profile,
            UUID calculationId,
            Instant calculatedAt) {

        return rules.stream()
                .map(rule -> rule.evaluate(profile, calculationId, calculatedAt))
                .flatMap(Optional::stream)
                .toList();
    }
}