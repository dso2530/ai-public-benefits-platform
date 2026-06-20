package com.govtech.profile.domain.valueobject;

public record Address(

        String street,

        String postalCode,

        String city,

        String country

) {

    public Address {

        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException(
                    "City is required");
        }

        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException(
                    "Country is required");
        }
    }
}