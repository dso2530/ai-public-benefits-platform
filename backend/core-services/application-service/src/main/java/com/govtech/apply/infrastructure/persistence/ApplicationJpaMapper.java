package com.govtech.apply.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.govtech.apply.domain.model.Application;

@Component
public class ApplicationJpaMapper {

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

  public ApplicationJpaEntity update(
      ApplicationJpaEntity entity,
      Application application) {

    entity.setAidCode(application.aidCode());
    entity.setAidName(application.aidName());
    entity.setStatus(application.status());
    entity.setObjectKey(application.objectKey());
    entity.setMissingDocuments(application.missingDocuments());

    return entity;
  }
}