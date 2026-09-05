package com.govtech.apply.api.mapper;

import org.springframework.stereotype.Component;

import com.govtech.apply.api.dto.ApplicationDto;
import com.govtech.apply.domain.model.Application;

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