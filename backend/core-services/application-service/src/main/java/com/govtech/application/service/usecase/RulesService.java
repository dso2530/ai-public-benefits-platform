package com.govtech.application.service.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.govtech.application.domain.model.DocumentType;
import com.govtech.application.service.dto.RequiredDocument;

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