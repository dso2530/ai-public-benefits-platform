package com.govtech.application.service.client.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record ProfileContractDto(

                String subject,
                String email,

                String firstName,
                String lastName,
                String nationality,
                LocalDate birthDate,

                String street,
                String postalCode,
                String city,

                String housingStatus,
                Integer childrenCount,
                Boolean singleParent,

                String referenceIncome,
                Integer taxYear,
                Integer assessmentYear,
                String socialSecurityNumber

) {
}