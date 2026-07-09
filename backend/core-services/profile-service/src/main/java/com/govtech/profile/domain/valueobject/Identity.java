package com.govtech.profile.domain.valueobject;

import java.time.LocalDate;

public record Identity(
        String firstName,
        String lastName,
        LocalDate birthDate,
        String birthPlace,
        String nationality)
        implements Mergeable<Identity> {

    @Override
    public Identity merge(Identity incoming) {

        if (incoming == null) {
            return this;
        }

        return new Identity(
                merge(firstName, incoming.firstName()),
                merge(lastName, incoming.lastName()),
                merge(birthDate, incoming.birthDate()),
                merge(birthPlace, incoming.birthPlace()),
                merge(nationality, incoming.nationality()));
    }

    private static <T> T merge(T current, T incoming) {
        return incoming != null ? incoming : current;
    }
}