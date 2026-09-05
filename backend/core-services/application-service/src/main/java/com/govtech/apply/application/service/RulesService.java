package com.govtech.apply.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.govtech.apply.application.dto.RequiredDocument;
import com.govtech.shared.model.DocumentType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RulesService {

    public List<RequiredDocument> requiredDocuments(String aidCode) {

        return switch (aidCode) {

            case "RSA" -> List.of(
                    new RequiredDocument(DocumentType.TAX_NOTICE, "Avis d'imposition", true),
                    new RequiredDocument(DocumentType.PROOF_OF_ADDRESS, "Justificatif de domicile récent", true),
                    new RequiredDocument(DocumentType.RIB, "Relevé d'Identité Bancaire", true));

            case "APL" -> List.of(
                    new RequiredDocument(DocumentType.LEASE, "Contrat de location / Bail", true),
                    new RequiredDocument(DocumentType.RIB, "Relevé d'Identité Bancaire", true));

            default -> List.of();
        };
    }
}