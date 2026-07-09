package com.govtech.eligibility.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AidType {

    RSA(
            "RSA",
            "Revenu de solidarité active"),

    APL(
            "APL",
            "Aide personnalisée au logement"),

    PRIME_ACTIVITE(
            "PRIME_ACTIVITE",
            "Prime d'activité");

    private final String code;

    private final String label;
}