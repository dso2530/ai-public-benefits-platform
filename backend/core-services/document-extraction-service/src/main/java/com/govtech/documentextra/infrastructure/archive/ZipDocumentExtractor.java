package com.govtech.documentextra.infrastructure.archive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Component;

import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.documentextra.domain.port.DocumentExtractor;
import com.govtech.documentextra.infrastructure.archive.exception.ArchiveExtractionException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ZipDocumentExtractor implements DocumentExtractor {

        private final List<DocumentExtractor> extractors;

        @Override
        public boolean supports(String contentType) {
                return "application/zip".equalsIgnoreCase(contentType);
        }

        @Override
        public ExtractionResult extract(InputStream inputStream) {

                List<String> texts = new ArrayList<>();

                int extractedFiles = 0;

                try (ZipInputStream zip = new ZipInputStream(inputStream)) {

                        ZipEntry entry;

                        while ((entry = zip.getNextEntry()) != null) {

                                if (entry.isDirectory()) {
                                        continue;
                                }

                                log.info(
                                                "Extracting archive entry {}",
                                                entry.getName());

                                byte[] content = readEntry(zip);

                                String contentType = detectContentType(
                                                entry.getName());

                                DocumentExtractor extractor = findExtractor(contentType);

                                ExtractionResult result = extractor.extract(
                                                new ByteArrayInputStream(content));

                                String text = result.text();

                                if (text != null && !text.isBlank()) {

                                        texts.add(
                                                        buildDocumentSection(
                                                                        entry.getName(),
                                                                        contentType,
                                                                        text));
                                }

                                extractedFiles++;

                                zip.closeEntry();
                        }

                } catch (Exception e) {

                        throw new ArchiveExtractionException(
                                        "Unable to extract ZIP archive",
                                        e);
                }

                Map<String, String> metadata = new HashMap<>();

                metadata.put(
                                "extractor",
                                "ZIP");

                metadata.put(
                                "filesExtracted",
                                String.valueOf(extractedFiles));

                return new ExtractionResult(
                                String.join("\n\n", texts),
                                "text/plain",
                                metadata);
        }

        private DocumentExtractor findExtractor(
                        String contentType) {

                return extractors.stream()
                                .filter(extractor -> extractor != this)
                                .filter(extractor -> extractor.supports(contentType))
                                .findFirst()
                                .orElseThrow(() -> new ArchiveExtractionException(
                                                "No extractor found for "
                                                                + contentType,
                                                null));
        }

        private String buildDocumentSection(
                        String filename,
                        String contentType,
                        String text) {

                return """
                                --- DOCUMENT ---
                                filename: %s
                                contentType: %s

                                %s
                                --- END DOCUMENT ---
                                """.formatted(
                                filename,
                                contentType,
                                text);
        }

        private byte[] readEntry(
                        InputStream input)
                        throws IOException {

                ByteArrayOutputStream output = new ByteArrayOutputStream();

                input.transferTo(output);

                return output.toByteArray();
        }

        private String detectContentType(
                        String filename) {

                String lower = filename.toLowerCase();

                if (lower.endsWith(".pdf")) {
                        return "application/pdf";
                }

                if (lower.endsWith(".txt")) {
                        return "text/plain";
                }

                if (lower.endsWith(".csv")) {
                        return "text/csv";
                }

                if (lower.endsWith(".json")) {
                        return "application/json";
                }

                if (lower.endsWith(".xml")) {
                        return "application/xml";
                }

                if (lower.endsWith(".zip")) {
                        return "application/zip";
                }

                return "application/octet-stream";
        }
}