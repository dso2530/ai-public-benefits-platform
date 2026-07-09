package com.govtech.profile.api.mapper;

import com.govtech.profile.api.dto.ProfileDto;
import com.govtech.profile.domain.model.Citizen;

public final class ProfileMapper {

  private ProfileMapper() {
  }

  public static ProfileDto toDto(Citizen citizen) {

    return ProfileDto.builder()
        .subject(citizen.getSubject())
        .email(citizen.getEmail())
        .firstName(citizen.getIdentity().firstName())
        .lastName(citizen.getIdentity().lastName())
        .city(citizen.getAddress().city())
        .postalCode(citizen.getAddress().postalCode())
        .housingStatus(citizen.getHousehold().housingStatus().name())
        .childrenCount(citizen.getHousehold().childrenCount())
        .singleParent(citizen.getHousehold().singleParent())
        .build();
  }
}
