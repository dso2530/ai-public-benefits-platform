package com.govtech.eligibility.domain.rule;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.eligibility.application.configuration.EligibilityProperties;
import com.govtech.eligibility.application.dto.ProfileContractDto;
import com.govtech.eligibility.domain.model.AidType;
import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.eligibility.domain.model.EligibilityStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RSAEligibilityRule implements EligibilityRule {

        private final EligibilityProperties properties;

        @Override
        public Optional<Eligibility> evaluate(
                        ProfileContractDto profile,
                        UUID calculationId,
                        Instant calculatedAt) {

                log.debug("Evaluating RSA eligibility for subject={}", profile.subject());

                if (profile.referenceIncome() == null) {
                        return Optional.empty();
                }

                BigDecimal income = ProfileContractDto.parse(profile.referenceIncome());

                boolean eligible = income.compareTo(properties.getRsa().getMaxIncome()) <= 0;

                log.info(
                                "RSA granted subject={}",
                                profile.subject());

                return Optional.of(
                                Eligibility.builder()
                                                .id(UUID.randomUUID())
                                                .calculationId(calculationId)
                                                .calculatedAt(calculatedAt)
                                                .subject(profile.subject())
                                                .aidCode(AidType.RSA.getCode())
                                                .aidName(AidType.RSA.getLabel())
                                                .status(
                                                                eligible
                                                                                ? EligibilityStatus.ELIGIBLE
                                                                                : EligibilityStatus.NOT_ELIGIBLE)
                                                .reason(
                                                                eligible
                                                                                ? "Revenu inférieur au plafond"
                                                                                : "Revenu supérieur au plafond")
                                                .estimatedAmount(new BigDecimal(
                                                                properties.getRsa().getEstimatedAmount()))
                                                .estimatedAmountLabel(properties.getRsa().getEstimatedAmountLabel())
                                                .build());
        }

}