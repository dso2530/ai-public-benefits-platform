package com.govtech.application.domain.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CerfaModel(

        String subject,
        String aidCode,

        String firstName,
        String lastName,
        LocalDate birthDate,
        String nationality,

        String email,

        String address,
        String postalCode,
        String city,

        String housingStatus,

        Integer childrenCount,
        Boolean singleParent,

        String referenceIncome,
        Integer taxYear,
        Integer assessmentYear) {
}