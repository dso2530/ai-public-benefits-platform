package com.govtech.application.service.cerfa;

import org.springframework.stereotype.Component;

import com.govtech.application.service.cerfa.model.CerfaFieldKey;
import com.govtech.application.service.client.dto.ProfileContractDto;

@Component
public class CerfaProfileMapper {

    public String resolveValue(CerfaFieldKey key, ProfileContractDto profile) {

        return switch (key) {
            case NOM -> profile.lastName();
            case PRENOM -> profile.firstName();
            case DATE_NAISSANCE -> profile.birthDate().toString();
            case NUMERO_SECURITE_SOCIALE -> profile.socialSecurityNumber();
            default -> null;
        };
    }
}