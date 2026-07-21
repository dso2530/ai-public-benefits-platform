package com.govtech.profile.domain.valueobject;

import java.math.BigDecimal;

public record HousingTax(
        Integer taxYear,
        String propertyReference,
        BigDecimal amount) implements Mergeable<HousingTax> {

    @Override
    public HousingTax merge(HousingTax incoming) {
        if (incoming == null) {
            return this;
        }

        return new HousingTax(
                merge(taxYear, incoming.taxYear()),
                merge(propertyReference, incoming.propertyReference()),
                merge(amount, incoming.amount()));
    }

    private static <T> T merge(T current, T incoming) {
        return incoming != null ? incoming : current;
    }
}