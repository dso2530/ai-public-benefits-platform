package com.govtech.profile.domain.valueobject;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentReceipt(
        String landlordName,
        BigDecimal monthlyRent,
        LocalDate receiptDate) implements Mergeable<RentReceipt> {

    @Override
    public RentReceipt merge(RentReceipt incoming) {
        if (incoming == null) {
            return this;
        }

        return new RentReceipt(
                merge(landlordName, incoming.landlordName()),
                merge(monthlyRent, incoming.monthlyRent()),
                merge(receiptDate, incoming.receiptDate()));
    }

    private static <T> T merge(T current, T incoming) {
        return incoming != null ? incoming : current;
    }
}