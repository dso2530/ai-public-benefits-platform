package com.govtech.profile.api.dto;

import java.time.LocalDate;

import com.govtech.profile.domain.model.Citizen;

import lombok.Builder;

@Builder
public record ProfileContractDto(

                String subject,
                String email,

                String firstName,
                String lastName,
                String nationality,
                LocalDate birthDate,
                String city,
                String postalCode,

                String street,
                String housingStatus,
                Integer childrenCount,
                Boolean singleParent,

                String referenceIncome,
                Integer taxYear,
                Integer assessmentYear

) {

        public static ProfileContractDto from(Citizen citizen) {

                return ProfileContractDto.builder()
                                .subject(citizen.getSubject())
                                .email(citizen.getEmail())

                                .firstName(citizen.getIdentity().firstName())
                                .lastName(citizen.getIdentity().lastName())
                                .nationality(citizen.getIdentity().nationality())

                                .city(citizen.getAddress().city())
                                .postalCode(citizen.getAddress().postalCode())
                                .street(citizen.getAddress().street())
                                .housingStatus(citizen.getHousehold().housingStatus().name())
                                .childrenCount(citizen.getHousehold().childrenCount())
                                .singleParent(citizen.getHousehold().singleParent())

                                .referenceIncome(citizen.getTaxInformation() != null
                                                ? citizen.getTaxInformation().referenceIncome()
                                                : null)
                                .taxYear(citizen.getTaxInformation() != null
                                                ? citizen.getTaxInformation().taxYear()
                                                : null)
                                .assessmentYear(citizen.getTaxInformation() != null
                                                ? citizen.getTaxInformation().assessmentYear()
                                                : null)

                                .build();
        }
}