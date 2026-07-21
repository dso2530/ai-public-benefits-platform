package com.govtech.document.domain.model;

public enum DocumentType {
  IDENTITY_CARD("Carte d'identité"),
  PROOF_OF_ADDRESS("Justificatif de domicile"),
  PASSPORT("Passport"),
  PAYSLIP("Fiche de paie"),
  RIB("Compte bancaire"),
  TAX_NOTICE("Avis d'imposition"),
  LEASE("Contrat de location"),
  OTHER("Autre");

  private final String name;

  DocumentType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
