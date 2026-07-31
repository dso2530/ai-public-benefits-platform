package com.govtech.consent.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.govtech.consent.domain.model.Consent;
import com.govtech.consent.domain.model.ConsentStatus;
import com.govtech.consent.infrastructure.persistence.ConsentJpaEntity;
import com.govtech.consent.infrastructure.persistence.ConsentStatusJpa;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConsentMapper {

    default Consent toDomain(
            ConsentJpaEntity entity) {

        if (entity == null) {
            return null;
        }

        return Consent.builder()

                .id(entity.getId())

                .userId(entity.getUserId())

                .purpose(entity.getPurpose())

                .version(entity.getVersion())

                .consentVersionId(
                        entity.getConsentVersionId())

                .status(
                        mapStatus(entity.getStatus()))

                .grantedAt(
                        entity.getGrantedAt())

                .revokedAt(
                        entity.getRevokedAt())

                .source(
                        entity.getSource())

                .build();
    }

    default ConsentJpaEntity toEntity(
            Consent domain) {

        if (domain == null) {
            return null;
        }

        return ConsentJpaEntity.builder()

                .id(domain.getId())

                .userId(domain.getUserId())

                .purpose(domain.getPurpose())

                .version(domain.getVersion())

                .consentVersionId(
                        domain.getConsentVersionId())

                .status(
                        mapStatus(domain.getStatus()))

                .grantedAt(
                        domain.getGrantedAt())

                .revokedAt(
                        domain.getRevokedAt())

                .source(
                        domain.getSource())

                .build();
    }

    default ConsentStatus mapStatus(
            ConsentStatusJpa status) {

        if (status == null) {
            return null;
        }

        return switch (status) {

            case GRANTED ->
                ConsentStatus.GRANTED;

            case REVOKED ->
                ConsentStatus.REVOKED;

            case EXPIRED ->
                ConsentStatus.EXPIRED;
        };
    }

    default ConsentStatusJpa mapStatus(
            ConsentStatus status) {

        if (status == null) {
            return null;
        }

        return switch (status) {

            case GRANTED ->
                ConsentStatusJpa.GRANTED;

            case REVOKED ->
                ConsentStatusJpa.REVOKED;

            case EXPIRED ->
                ConsentStatusJpa.EXPIRED;
        };
    }

}