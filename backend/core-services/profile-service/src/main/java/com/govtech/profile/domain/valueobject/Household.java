package com.govtech.profile.domain.valueobject;

public record Household(HousingStatus housingStatus, Integer childrenCount, Boolean singleParent) {

  public Household {

    if (childrenCount == null) {
      childrenCount = 0;
    }

    if (childrenCount < 0) {
      throw new IllegalArgumentException("childrenCount cannot be negative");
    }
  }

  public boolean hasChildren() {
    return childrenCount > 0;
  }
}
