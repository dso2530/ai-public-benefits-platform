package com.govtech.application.api.mapper;

import org.springframework.stereotype.Component;

import com.govtech.application.api.dto.ApplicationDto;
import com.govtech.application.domain.model.Application;

@Component
public class ApplicationDtoMapper {

    public ApplicationDto toDto(
            Application application) {

        return new ApplicationDto(
                application.applicationId(),
                application.subject(),
                application.aidCode(),
                application.aidName(),
                application.status(),
                application.createdAt(),
                application.missingDocuments());
    }
}