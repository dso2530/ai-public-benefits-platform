package com.govtech.eligibility.domain.rule;

import com.govtech.eligibility.application.configuration.EligibilityProperties;
import com.govtech.eligibility.application.dto.ProfileContractDto;
import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.eligibility.domain.model.EligibilityStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CSSEligibilityRule implements EligibilityRule {

    private final EligibilityProperties properties;

    @Override
    public Optional<Eligibility> evaluate(ProfileContractDto profile,
            UUID calculationId,
            Instant calculatedAt) {

        log.debug("Evaluating CSS for subject={}", profile.subject());

        if (!isEligible(profile)) {
            return Optional.of(
                    Eligibility.builder()
                            .calculationId(calculationId)
                            .calculatedAt(calculatedAt)
                            .subject(profile.subject())
                            .aidCode("CSS")
                            .aidName("Complémentaire Santé Solidaire")
                            .status(EligibilityStatus.NOT_ELIGIBLE)
                            .reason("Les critères d'éligibilité ne sont pas remplis.")
                            .build());
        }

        return Optional.of(
                Eligibility.builder()
                        .calculationId(calculationId)
                        .calculatedAt(calculatedAt)
                        .subject(profile.subject())
                        .aidCode("CSS")
                        .aidName("Complémentaire Santé Solidaire")
                        .status(EligibilityStatus.ELIGIBLE)
                        .estimatedAmount(new BigDecimal(
                                properties.getCss().getEstimatedAmount()))
                        .estimatedAmountLabel(
                                properties.getCss().getEstimatedAmountLabel())
                        .reason("Vous pouvez bénéficier de la Complémentaire Santé Solidaire.")
                        .build());
    }

    private boolean isEligible(ProfileContractDto profile) {

        if (profile.referenceIncome() == null || profile.birthDate() == null) {
            return false;
        }

        BigDecimal income = ProfileContractDto.parse(profile.referenceIncome());

        int age = Period.between(profile.birthDate(), LocalDate.now()).getYears();

        return age >= 18
                && income.compareTo(properties.getCss().getIncomeCeiling()) <= 0;
    }
}