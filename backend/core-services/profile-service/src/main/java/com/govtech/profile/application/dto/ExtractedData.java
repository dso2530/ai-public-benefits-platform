package com.govtech.profile.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExtractedData(

        // Identité
        String firstName,
        String lastName,
        LocalDate birthDate,
        String birthPlace,
        String nationality,

        // Adresse
        String street,
        String postalCode,
        String city,
        String country,

        // Fiscal
        String taxNumber,
        Integer taxYear,
        Integer assessmentYear,
        String referenceIncome,
        BigDecimal numberOfShares,
        String incomeTax,
        String refundAmount,
        String amountToPay,
        LocalDate refundDate,

        // Bancaire
        String iban,
        String bic,

        // Foyer
        Integer childrenCount,
        Boolean singleParent,
        String housingStatus

) {
}