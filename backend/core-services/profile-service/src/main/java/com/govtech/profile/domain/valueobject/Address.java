package com.govtech.profile.domain.valueobject;

public record Address(
    String street,
    String postalCode,
    String city,
    String country)
    implements Mergeable<Address> {

  @Override
  public Address merge(Address incoming) {

    if (incoming == null) {
      return this;
    }

    return new Address(
        merge(street, incoming.street()),
        merge(postalCode, incoming.postalCode()),
        merge(city, incoming.city()),
        merge(country, incoming.country()));
  }

  private static <T> T merge(T current, T incoming) {
    return incoming != null ? incoming : current;
  }
}