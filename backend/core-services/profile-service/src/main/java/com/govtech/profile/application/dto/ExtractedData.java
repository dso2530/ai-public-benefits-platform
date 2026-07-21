package com.govtech.profile.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.govtech.profile.domain.valueobject.CafInformation;
import com.govtech.profile.domain.valueobject.EnergyBill;
import com.govtech.profile.domain.valueobject.HomeInsurance;
import com.govtech.profile.domain.valueobject.HousingInformation;
import com.govtech.profile.domain.valueobject.HousingTax;
import com.govtech.profile.domain.valueobject.Lease;
import com.govtech.profile.domain.valueobject.RentReceipt;

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
        String housingStatus,

        // Logement
        HousingInformation housingInformation,
        Lease lease,
        RentReceipt rentReceipt,
        EnergyBill energyBill,
        HomeInsurance homeInsurance,
        HousingTax housingTax,

        // CAF
        CafInformation cafInformation

) {
}