package com.gouvtech.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import com.govtech.security.application.even.DocumentScanEventService;
import com.govtech.security.application.usecase.ScanDocumentUseCase;
import com.govtech.security.domain.model.SecurityScan;
import com.govtech.security.domain.model.SecurityStatus;
import com.govtech.security.domain.port.DocumentAnalysis;
import com.govtech.security.domain.port.DocumentAnalyzerPort;
import com.govtech.security.domain.port.SecurityScanRepositoryPort;
import com.govtech.security.infrastructure.scanner.ClamAvClient;
import com.govtech.security.infrastructure.scanner.ClamAvProperties;
import com.govtech.security.infrastructure.scanner.ClamAvScannerAdapter;
import com.govtech.security.infrastructure.tika.TikaDocumentAnalyzerAdapter;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = {
                ScanDocumentUseCaseTest.TestConfig.class,
                ScanDocumentUseCase.class,
                ClamAvScannerAdapter.class,
                ClamAvClient.class,
                TikaDocumentAnalyzerAdapter.class
})
@ActiveProfiles("test")
class ScanDocumentUseCaseTest {

        @Autowired
        private ScanDocumentUseCase useCase;

        @MockitoBean
        private SecurityScanRepositoryPort repository;

        @MockitoBean
        private DocumentScanEventService eventService;

        @MockitoBean
        private DocumentAnalyzerPort analyzer;

        @TestConfiguration
        @EnableConfigurationProperties(ClamAvProperties.class)
        static class TestConfig {

                ClamAvProperties clamAvProperties() {
                        return new ClamAvProperties(
                                        "localhost",
                                        3310, 6000);
                }
        }

        @Test
        void shouldDetectEicar() throws Exception {

                byte[] content = getClass()
                                .getClassLoader()
                                .getResourceAsStream("eicar.txt")
                                .readAllBytes();

                System.out.println("SIZE=" + content.length);

                when(repository.save(any(SecurityScan.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                when(analyzer.analyze(any()))
                                .thenReturn(
                                                new DocumentAnalysis(
                                                                "application/pdf",
                                                                "pdf",
                                                                69,
                                                                null));

                SecurityScan scan = useCase.execute(
                                1L,
                                content,
                                "sha256");

                assertEquals(SecurityStatus.INFECTED, scan.getStatus());

                verify(repository).save(any(SecurityScan.class));
                verify(eventService).publishScanCompleted(any(SecurityScan.class));
        }

        @Test
        void shouldAcceptCleanFile() throws Exception {

                byte[] content = Objects.requireNonNull(
                                getClass()
                                                .getClassLoader()
                                                .getResourceAsStream("clean.txt"),
                                "clean.txt not found in test resources")
                                .readAllBytes();

                System.out.println("SIZE=" + content.length);

                when(analyzer.analyze(any()))
                                .thenReturn(
                                                new DocumentAnalysis(
                                                                "application/pdf",
                                                                "pdf",
                                                                69,
                                                                null));

                when(repository.save(any(SecurityScan.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                SecurityScan scan = useCase.execute(
                                2L,
                                content,
                                "sha256");

                assertEquals(
                                SecurityStatus.CLEAN,
                                scan.getStatus());

                assertEquals(
                                "CLAMAV",
                                scan.getScanEngine());

                verify(repository)
                                .save(any(SecurityScan.class));

                verify(eventService)
                                .publishScanCompleted(any(SecurityScan.class));
        }

}