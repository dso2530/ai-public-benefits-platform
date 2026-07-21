// src/constants/document-labels.ts

import { DocumentType } from "../types/document-type";

export const documentLabels: Record<DocumentType, string> = {

    [DocumentType.TAX_NOTICE]:
        "Avis d'imposition",

    [DocumentType.PROOF_OF_ADDRESS]:
        "Justificatif d'adresse",

    [DocumentType.RIB]:
        "Relevé d'identité bancaire (RIB)",

    [DocumentType.IDENTITY_CARD]:
        "Pièce d'identité",

    [DocumentType.PAYSLIP]:
        "Bulletin de salaire",

    [DocumentType.CAF_CERTIFICATE]:
        "Attestation CAF",

    [DocumentType.LEASE]:
        "Contrat de location",
};