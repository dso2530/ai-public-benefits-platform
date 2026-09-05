package com.gouvtech.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.govtech.platform.messaging.event.EventContext;
import com.govtech.security.application.command.DocumentScanRequest;
import com.govtech.security.application.event.DocumentScanEventService;
import com.govtech.security.application.usecase.ScanDocumentUseCase;
import com.govtech.security.domain.model.DocumentAnalysis;

import com.govtech.security.domain.model.SecurityScan;
import com.govtech.security.domain.model.SecurityStatus;
import com.govtech.security.domain.port.DocumentAnalyzerPort;
import com.govtech.security.domain.port.SecurityScanRepositoryPort;
import com.govtech.security.infrastructure.scanner.ClamAvClient;
import com.govtech.security.infrastructure.scanner.ClamAvProperties;
import com.govtech.security.infrastructure.scanner.ClamAvScannerAdapter;
import com.govtech.security.infrastructure.tika.TikaDocumentAnalyzerAdapter;
import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

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
                                        3310,
                                        6000);
                }
        }

        @Test
        void shouldDetectEicar() throws Exception {

                byte[] content = Objects.requireNonNull(
                                getClass()
                                                .getClassLoader()
                                                .getResourceAsStream("eicar.txt"),
                                "eicar.txt not found in test resources")
                                .readAllBytes();

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
                                createRequest(
                                                1L,
                                                content));

                assertEquals(
                                SecurityStatus.INFECTED,
                                scan.getStatus());

                verify(repository).save(any(SecurityScan.class));
        }

        @Test
        void shouldAcceptCleanFile() throws Exception {

                byte[] content = Objects.requireNonNull(
                                getClass()
                                                .getClassLoader()
                                                .getResourceAsStream("clean.txt"),
                                "clean.txt not found in test resources")
                                .readAllBytes();

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
                                createRequest(
                                                2L,
                                                content));

                assertEquals(
                                SecurityStatus.CLEAN,
                                scan.getStatus());

                assertEquals(
                                "CLAMAV",
                                scan.getScanEngine());

                verify(repository)
                                .save(any(SecurityScan.class));
        }

        private DocumentScanRequest createRequest(
                        Long documentId,
                        byte[] content) {

                return DocumentScanRequest.builder()
                                .documentId(documentId)
                                .content(content)
                                .sha256("sha256")
                                .origin(DocumentOrigin.USER_UPLOAD)

                                .bucket("documents-user-quarantine")
                                .objectKey(
                                                "citizens/test-user/document.pdf")
                                .contentType("application/pdf")
                                .fileName("document.pdf")
                                .documentType(DocumentType.TAX_NOTICE)

                                .metadata(Map.of())

                                .eventContext(
                                                EventContext.builder()
                                                                .correlationId("correlation-id")
                                                                .causationId("causation-id")
                                                                .build())

                                .build();
        }
}