
package com.govtech.profile.infrastructure.persistence;

import com.govtech.profile.domain.model.Citizen;
import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.Household;

public final class CitizenJpaMapper {

    private CitizenJpaMapper() {
    }

    public static Citizen toDomain(
            CitizenJpaEntity entity) {

        return new Citizen(
                entity.getId(),
                entity.getSubject(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                new Address(
                        entity.getStreet(),
                        entity.getPostalCode(),
                        entity.getCity(),
                        entity.getCountry()
                ),
                new Household(
                        entity.getHousingStatus(),
                        entity.getChildrenCount(),
                        entity.getSingleParent()
                )
        );
    }

    public static CitizenJpaEntity toEntity(
            Citizen citizen) {

        return CitizenJpaEntity.builder()
                .id(citizen.getId())
                .subject(citizen.getSubject())
                .email(citizen.getEmail())
                .firstName(citizen.getFirstName())
                .lastName(citizen.getLastName())
                .street(citizen.getAddress().street())
                .postalCode(citizen.getAddress().postalCode())
                .city(citizen.getAddress().city())
                .country(citizen.getAddress().country())
                .housingStatus(
                        citizen.getHousehold()
                                .housingStatus())
                .childrenCount(
                        citizen.getHousehold()
                                .childrenCount())
                .singleParent(
                        citizen.getHousehold()
                                .singleParent())
                .build();
    }
}