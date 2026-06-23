package com.govtech.document.application.dto;

public enum DocumentType {

    IDENTITY_CARD("Carte d'identité"),
    PROOF_OF_ADDRESS("Justificatif de domicile"),
    TAX_NOTICE("Avis d'imposition");

    private final String name;

    DocumentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}