package com.govtech.profile.domain.valueobject;


public record Household(
    HousingStatus housingStatus,
    Integer childrenCount,
    Boolean singleParent)
    implements Mergeable<Household> {

  public Household {

    if (childrenCount == null) {
      childrenCount = 0;
    }

    if (childrenCount < 0) {
      throw new IllegalArgumentException(
          "childrenCount cannot be negative");
    }

    if (singleParent == null) {
      singleParent = false;
    }
  }

  public boolean hasChildren() {
    return childrenCount > 0;
  }

  @Override
  public Household merge(Household incoming) {

    if (incoming == null) {
      return this;
    }

    return new Household(
        merge(housingStatus, incoming.housingStatus()),
        merge(childrenCount, incoming.childrenCount()),
        merge(singleParent, incoming.singleParent()));
  }

  private static <T> T merge(T current, T incoming) {
    return incoming != null ? incoming : current;
  }
}