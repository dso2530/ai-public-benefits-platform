package com.govtech.security.domain.port;


import com.govtech.security.domain.model.SecurityScan;


public interface SecurityScanRepositoryPort {


    SecurityScan save(
            SecurityScan scan
    );

}