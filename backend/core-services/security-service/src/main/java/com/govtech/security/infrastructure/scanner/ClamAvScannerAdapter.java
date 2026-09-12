package com.govtech.security.infrastructure.scanner;

import org.springframework.stereotype.Component;

import com.govtech.security.domain.model.ScanResult;
import com.govtech.security.domain.model.SecurityStatus;
import com.govtech.security.domain.port.MalwareScannerPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClamAvScannerAdapter implements MalwareScannerPort {
        private final ClamAvClient client;

        @Override
        public ScanResult scan(byte[] content) {

                log.info(
                                "Starting malware scan with ClamAV, content size={} bytes",
                                content != null ? content.length : null);

                boolean infected = client.scan(content);

                log.info(
                                "ClamAV scan result infected={}",
                                infected);

                if (infected) {
                        log.warn(
                                        "Malware detected by ClamAV");

                        return new ScanResult(SecurityStatus.INFECTED, "CLAMAV");
                }

                  log.info(
                    "Document clean according to ClamAV"
                );

                return new ScanResult(SecurityStatus.CLEAN, "CLAMAV");
        }
}