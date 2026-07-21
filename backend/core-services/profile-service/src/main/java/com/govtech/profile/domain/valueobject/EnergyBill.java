package com.govtech.profile.domain.valueobject;

import java.time.LocalDate;

public record EnergyBill(
        String provider,
        String contractReference,
        LocalDate documentDate)
        implements Mergeable<EnergyBill> {

    @Override
    public EnergyBill merge(EnergyBill incoming) {

        if (incoming == null) {
            return this;
        }

        return new EnergyBill(
                merge(provider, incoming.provider()),
                merge(contractReference, incoming.contractReference()),
                merge(documentDate, incoming.documentDate()));
    }

    private static <T> T merge(T current, T incoming) {
        return incoming != null ? incoming : current;
    }
}