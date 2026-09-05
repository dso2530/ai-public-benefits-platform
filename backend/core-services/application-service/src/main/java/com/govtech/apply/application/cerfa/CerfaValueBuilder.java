package com.govtech.apply.application.cerfa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.govtech.apply.application.cerfa.model.CerfaFieldKey;
import com.govtech.apply.application.client.dto.ProfileContractDto;
import com.govtech.apply.infrastructure.persistence.ApplicationJpaEntity;

@Component
public class CerfaValueBuilder {

    public Map<CerfaFieldKey, String> build(
            ProfileContractDto profile,
            ApplicationJpaEntity application) {

        Map<CerfaFieldKey, String> values = new EnumMap<>(CerfaFieldKey.class);

        values.put(
                CerfaFieldKey.NOM,
                profile.lastName());

        values.put(
                CerfaFieldKey.PRENOM,
                profile.firstName());

        values.put(
                CerfaFieldKey.DATE_NAISSANCE,
                formatDate(profile.birthDate()));

        values.put(
                CerfaFieldKey.NUMERO_SECURITE_SOCIALE,
                profile.socialSecurityNumber());

        return values;
    }

    private String formatDate(LocalDate date) {
        return date == null
                ? ""
                : date.format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}