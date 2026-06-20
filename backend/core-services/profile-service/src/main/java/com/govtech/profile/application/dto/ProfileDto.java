
package com.govtech.profile.application.dto;
import com.govtech.profile.domain.model.Citizen;

import lombok.Builder;

@Builder
public record ProfileDto(

        String subject,

        String email,

        String firstName,

        String lastName,

        String city,

        String postalCode,

        String housingStatus,

        Integer childrenCount,

        Boolean singleParent

) {

    public static ProfileDto from(
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