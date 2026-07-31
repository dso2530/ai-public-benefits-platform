package com.govtech.consent.api.dto;

import java.time.Instant;
import java.util.UUID;

import com.govtech.consent.domain.model.Consent;

public record ConsentResponse(

        UUID id,

        UUID userId,

        String purpose,

        String version,

        String status,

        Instant grantedAt

) {

    public static ConsentResponse from(
            Consent consent) {

        return new ConsentResponse(
                consent.getId(),
                consent.getUserId(),
                consent.getPurpose(),
                consent.getVersion(),
                consent.getStatus().name(),
                consent.getGrantedAt());

    }

}