package com.govtech.profile.domain.valueobject;

import java.time.LocalDate;

public record HomeInsurance(
        String insurer,
        String policyNumber,
        LocalDate contractStartDate,
        LocalDate contractEndDate) implements Mergeable<HomeInsurance> {

    @Override
    public HomeInsurance merge(HomeInsurance incoming) {
        if (incoming == null) {
            return this;
        }

        return new HomeInsurance(
                merge(insurer, incoming.insurer()),
                merge(policyNumber, incoming.policyNumber()),
                merge(contractStartDate, incoming.contractStartDate()),
                merge(contractEndDate, incoming.contractEndDate()));
    }

    private static <T> T merge(T current, T incoming) {
        return incoming != null ? incoming : current;
    }
}