package com.govtech.eligibility.domain.model;

public enum EligibilityStatus {

    ELIGIBLE,
    NOT_ELIGIBLE,
    UNKNOWN;

      public boolean isEligible() {
        return this == ELIGIBLE;
    }
}

