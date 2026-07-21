package com.govtech.profile.domain.valueobject;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Lease(
        String landlordName,
        BigDecimal monthlyRent,
        LocalDate entryDate) implements Mergeable<Lease> {

    @Override
    public Lease merge(Lease incoming) {
        if (incoming == null) {
            return this;
        }

        return new Lease(
                merge(landlordName, incoming.landlordName()),
                merge(monthlyRent, incoming.monthlyRent()),
                merge(entryDate, incoming.entryDate()));
    }

    private static <T> T merge(T current, T incoming) {
        return incoming != null ? incoming : current;
    }
}