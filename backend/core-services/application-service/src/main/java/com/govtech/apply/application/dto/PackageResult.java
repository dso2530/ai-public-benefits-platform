package com.govtech.apply.application.dto;


import com.govtech.apply.domain.model.ApplicationStatus;
import com.govtech.apply.domain.model.PackageStatus;

public record PackageResult(
                byte[] zip,
                PackageStatus status,
                ApplicationStatus applicationStatus) {
}