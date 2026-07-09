package com.govtech.profile.domain.valueobject;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TaxInformation(
        String taxNumber,
        Integer taxYear,
        Integer assessmentYear,
        String referenceIncome,
        BigDecimal numberOfShares,
        String incomeTax,
        String refundAmount,
        String amountToPay,
        LocalDate refundDate)
        implements Mergeable<TaxInformation> {

    @Override
    public TaxInformation merge(TaxInformation incoming) {

        if (incoming == null) {
            return this;
        }

        return new TaxInformation(
                merge(taxNumber, incoming.taxNumber()),
                merge(taxYear, incoming.taxYear()),
                merge(assessmentYear, incoming.assessmentYear()),
                merge(referenceIncome, incoming.referenceIncome()),
                merge(numberOfShares, incoming.numberOfShares()),
                merge(incomeTax, incoming.incomeTax()),
                merge(refundAmount, incoming.refundAmount()),
                merge(amountToPay, incoming.amountToPay()),
                merge(refundDate, incoming.refundDate()));
    }

    private static <T> T merge(T current, T incoming) {
        return incoming != null ? incoming : current;
    }
}