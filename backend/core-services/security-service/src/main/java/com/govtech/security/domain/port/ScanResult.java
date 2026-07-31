package com.govtech.security.domain.port;


import com.govtech.security.domain.model.SecurityStatus;


public record ScanResult(

        SecurityStatus status,

        String engine

) {}