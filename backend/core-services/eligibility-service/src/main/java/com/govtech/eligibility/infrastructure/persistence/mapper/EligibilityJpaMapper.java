package com.govtech.eligibility.infrastructure.persistence.mapper;

import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.eligibility.domain.model.EligibilityStatus;
import com.govtech.eligibility.infrastructure.persistence.entity.EligibilityJpaEntity;

public final class EligibilityJpaMapper {

    private EligibilityJpaMapper() {
    }

    public static EligibilityJpaEntity toEntity(Eligibility eligibility) {

        return EligibilityJpaEntity.builder()
                .calculationId(eligibility.calculationId())
                .calculatedAt(eligibility.calculatedAt())
                .subject(eligibility.subject())
                .aidCode(eligibility.aidCode())
                .aidName(eligibility.aidName())
                .status(EligibilityStatus.valueOf(eligibility.status().name()))
                .reason(eligibility.reason())
                .estimatedAmount(eligibility.estimatedAmount())
                .estimatedAmountLabel(eligibility.estimatedAmountLabel())
                .build();
    }

    public static Eligibility toDomain(EligibilityJpaEntity entity) {

        return Eligibility.builder()
                .id(entity.getId())
                .calculationId(entity.getCalculationId())
                .calculatedAt(entity.getCalculatedAt())
                .subject(entity.getSubject())
                .aidCode(entity.getAidCode())
                .aidName(entity.getAidName())
                .status(entity.getStatus())
                .reason(entity.getReason())
                .estimatedAmount(entity.getEstimatedAmount())
                .estimatedAmountLabel(entity.getEstimatedAmountLabel())
                .build();
    }
}