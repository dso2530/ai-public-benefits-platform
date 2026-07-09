package com.govtech.profile.application.dto;

import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.BankAccount;
import com.govtech.profile.domain.valueobject.Household;
import com.govtech.profile.domain.valueobject.HousingStatus;
import com.govtech.profile.domain.valueobject.Identity;
import com.govtech.profile.domain.valueobject.TaxInformation;

import lombok.Builder;

@Builder
public record UpdateProfileCommand(
                Identity identity,
                Address address,
                Household household,
                TaxInformation taxInformation,
                BankAccount bankAccount) {

        public static UpdateProfileCommand from(ExtractedData extracted) {
                return new UpdateProfileCommand(
                                new Identity(
                                                extracted.firstName(),
                                                extracted.lastName(),
                                                extracted.birthDate(), extracted.birthPlace(), extracted.nationality()),
                                new Address(
                                                extracted.street(),
                                                extracted.postalCode(),
                                                extracted.city(),
                                                extracted.country()),
                                new Household(HousingStatus.valueOf(extracted.housingStatus()),
                                                extracted.childrenCount(),
                                                extracted.singleParent()),
                                new TaxInformation(
                                                extracted.taxNumber(),
                                                extracted.taxYear(),
                                                extracted.assessmentYear(),
                                                extracted.referenceIncome(),
                                                extracted.numberOfShares(),
                                                extracted.incomeTax(),
                                                extracted.refundAmount(),
                                                extracted.amountToPay(),
                                                extracted.refundDate()),
                                new BankAccount(
                                                extracted.iban(),
                                                extracted.bic()));
        }
}