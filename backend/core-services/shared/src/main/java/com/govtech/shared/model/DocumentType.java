package com.govtech.shared.model;

public enum DocumentType {
  IDENTITY_CARD("Carte d'identité"),
  PROOF_OF_ADDRESS("Justificatif de domicile"),
  PASSPORT("Passport"),
  PAYSLIP("Fiche de paie"),
  RIB("Compte bancaire"),
  TAX_NOTICE("Avis d'imposition"),
  LEASE("Contrat de location"),
  RENT_RECEIPT("Quittance de loyer"),
  ENERGY_BILL("Facture d'énergie"), 
  HOME_INSURANCE("Assurance habitation"),
  HOUSING_TAX("Taxe d'habitation"), 
  CAF_CERTIFICATE("Attestation CAF"),
  OTHER("Autre");

  private final String name;

  DocumentType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}


