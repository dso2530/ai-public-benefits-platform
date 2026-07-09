package com.govtech.profile.infrastructure.persistence.mapper;

import com.govtech.profile.domain.valueobject.Household;
import com.govtech.profile.infrastructure.persistence.entity.HouseholdEmbeddable;

public final class HouseholdJpaMapper {

    private HouseholdJpaMapper() {
    }

    public static Household toDomain(HouseholdEmbeddable entity) {
        if (entity == null) {
            return null;
        }

        return new Household(
                entity.getHousingStatus(),
                entity.getChildrenCount(),
                entity.getSingleParent());
    }

    public static HouseholdEmbeddable toEntity(Household household) {
        if (household == null) {
            return null;
        }

        return new HouseholdEmbeddable(
                household.housingStatus(),
                household.childrenCount(),
                household.singleParent());
    }
}