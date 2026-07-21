package com.govtech.application.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.govtech.application.domain.model.Application;

@Component
public class ApplicationJpaMapper {

  public ApplicationJpaEntity toEntity(Application application) {
    return ApplicationJpaEntity.builder()
        .applicationId(application.applicationId())
        .subject(application.subject())
        .aidCode(application.aidCode())
        .aidName(application.aidName())
        .status(application.status())
        .createdAt(application.createdAt())
        .objectKey(application.objectKey())
        .missingDocuments(null)
        .build();
  }

  public Application toDomain(ApplicationJpaEntity entity) {
    return Application.builder()
        .applicationId(entity.getApplicationId())
        .subject(entity.getSubject())
        .aidCode(entity.getAidCode())
        .aidName(entity.getAidName())
        .status(entity.getStatus())
        .createdAt(entity.getCreatedAt())
        .objectKey(entity.getObjectKey())
        .missingDocuments(entity.getMissingDocuments())
        .build();
  }

  public ApplicationJpaEntity toJpaEntity(Application application) {

    return ApplicationJpaEntity.builder()
        .applicationId(application.applicationId())
        .subject(application.subject())
        .aidCode(application.aidCode())
        .aidName(application.aidName())
        .status(application.status())
        .objectKey(application.objectKey())
        .createdAt(application.createdAt())
        .missingDocuments(application.missingDocuments())
        .build();
  }
}