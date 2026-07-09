package com.govtech.eligibility.application.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ProfileContractDto(

                String subject,

                String email,

                String firstName,

                String lastName,

                String city,

                String postalCode,

                String housingStatus,

                Integer childrenCount,

                Boolean singleParent,

                String referenceIncome,

                String incomeTax,

                String refundAmount,

                String amountToPay,

                Double numberOfShares) {

        public static BigDecimal parse(String value) {

                return new BigDecimal(
                                value.replace("€", "")
                                                .replace(" ", "")
                                                .replace(",", "."));
        }
}
