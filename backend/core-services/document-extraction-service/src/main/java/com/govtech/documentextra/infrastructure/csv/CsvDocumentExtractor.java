package com.govtech.documentextra.infrastructure.csv;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.documentextra.domain.port.DocumentExtractor;
import com.govtech.documentextra.infrastructure.csv.exception.CsvDocumentExtractorException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CsvDocumentExtractor implements DocumentExtractor {

        @Override
        public boolean supports(String contentType) {

                return "text/csv".equalsIgnoreCase(contentType)
                                || "application/csv".equalsIgnoreCase(contentType);
        }

        @Override
        public ExtractionResult extract(InputStream inputStream) {

                StringBuilder text = new StringBuilder();

                int rows = 0;

                try (
                                var reader = new InputStreamReader(
                                                inputStream,
                                                StandardCharsets.UTF_8)) {

                        CSVFormat format = CSVFormat.DEFAULT.builder()
                                        .setHeader()
                                        .setSkipHeaderRecord(true)
                                        .setIgnoreEmptyLines(true)
                                        .setTrim(true)
                                        .get();

                        Iterable<CSVRecord> records = format.parse(reader);

                        for (CSVRecord record : records) {

                                appendRecord(
                                                text,
                                                record);

                                rows++;
                        }

                } catch (Exception e) {

                        log.error(
                                        "CSV extraction failed",
                                        e);

                        throw new CsvDocumentExtractorException(
                                        "CSV extraction failed",
                                        e);
                }

                Map<String, String> metadata = new LinkedHashMap<>();

                metadata.put(
                                "extractor",
                                "CSV");

                metadata.put(
                                "rows",
                                String.valueOf(rows));

                return new ExtractionResult(
                                text.toString(),
                                "text/plain",
                                metadata);
        }

        private void appendRecord(
                        StringBuilder text,
                        CSVRecord record) {

                for (String column : record.getParser().getHeaderNames()) {

                        String value = record.get(column);

                        if (value == null || value.isBlank()) {
                                continue;
                        }

                        text.append(column)
                                        .append(": ")
                                        .append(value)
                                        .append("\n");
                }

                text.append("\n");
        }
}