package com.govtech.profile.infrastructure.persistence.mapper;

import com.govtech.profile.domain.model.Citizen;
import com.govtech.profile.infrastructure.persistence.entity.CitizenJpaEntity;

public final class CitizenJpaMapper {

  private CitizenJpaMapper() {
  }

  public static Citizen toDomain(CitizenJpaEntity entity) {

    return new Citizen(
        entity.getId(),
        entity.getSubject(),
        entity.getEmail(),
        entity.getIdentity(),
        entity.getAddress(),
        HouseholdJpaMapper.toDomain(entity.getHousehold()),
        entity.getTaxInformation(),
        entity.getBankAccount());
  }

  public static CitizenJpaEntity toEntity(Citizen citizen) {

    return CitizenJpaEntity.builder()
        .id(citizen.getId())
        .subject(citizen.getSubject())
        .email(citizen.getEmail())
        .identity(citizen.getIdentity())
        .address(citizen.getAddress())
        .household(HouseholdJpaMapper.toEntity(citizen.getHousehold()))
        .taxInformation(citizen.getTaxInformation())
        .bankAccount(citizen.getBankAccount())
        .build();
  }
}