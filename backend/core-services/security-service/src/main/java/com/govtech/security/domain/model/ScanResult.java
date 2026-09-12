package com.govtech.security.domain.model;

public record ScanResult(

        SecurityStatus status,

        String engine

) {}