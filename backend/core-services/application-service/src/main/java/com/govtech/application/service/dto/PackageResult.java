package com.govtech.application.service.dto;


import com.govtech.application.domain.model.ApplicationStatus;
import com.govtech.application.domain.model.PackageStatus;

public record PackageResult(
                byte[] zip,
                PackageStatus status,
                ApplicationStatus applicationStatus) {
}