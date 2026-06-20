package com.govtech.profile.api;

import com.govtech.profile.application.dto.ProfileDto;
import com.govtech.profile.domain.model.Citizen;

public final class ProfileMapper {

    private ProfileMapper() {
    }

    public static ProfileDto toDto(
            Citizen citizen) {

        return ProfileDto.builder()
                .subject(citizen.getSubject())
                .email(citizen.getEmail())
                .firstName(citizen.getFirstName())
                .lastName(citizen.getLastName())
                .city(citizen.getAddress().city())
                .postalCode(citizen.getAddress().postalCode())
                .housingStatus(
                        citizen.getHousehold()
                                .housingStatus()
                                .name())
                .childrenCount(
                        citizen.getHousehold()
                                .childrenCount())
                .singleParent(
                        citizen.getHousehold()
                                .singleParent())
                .build();
    }
}