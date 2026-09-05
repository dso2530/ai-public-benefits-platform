package com.govtech.documentextra.infrastructure.html;

import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.documentextra.domain.port.DocumentExtractor;
import com.govtech.documentextra.infrastructure.html.exception.HtmlDocumentExtractorException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
@Slf4j
public class HtmlDocumentExtractor implements DocumentExtractor {

        @Override
        public boolean supports(String contentType) {

                boolean supported = contentType != null
                                && contentType.toLowerCase(Locale.ROOT)
                                                .contains("text/html");

                log.debug(
                                "HTML extractor support check: contentType={}, supported={}",
                                contentType,
                                supported);

                return supported;
        }

        @Override
        public ExtractionResult extract(InputStream inputStream) {

                log.debug("Starting HTML document extraction");

                String html = readContent(inputStream);

                if (isAntiBotPage(html)) {
                        log.warn(
                                        "Anti-bot HTML page detected. Document will not be indexed.");

                        throw new HtmlDocumentExtractorException(
                                        "HTML document contains an anti-bot page",
                                        null);
                }

                try {
                        String text = Jsoup.parse(html).text();

                        log.info(
                                        "HTML document successfully extracted: characters={}",
                                        text.length());

                        return ExtractionResult.builder()
                                        .text(text)
                                        .build();

                } catch (Exception e) {
                        log.error(
                                        "Failed to extract text from HTML document",
                                        e);

                        throw new HtmlDocumentExtractorException(
                                        "Failed to extract text from HTML document",
                                        e);
                }
        }

        private boolean isAntiBotPage(String html) {

                if (html == null || html.isBlank()) {
                        return false;
                }

                String content = html.toLowerCase(Locale.ROOT);

                boolean detected = content.contains("made us think that you are a bot")
                                || content.contains("your activity and behavior")
                                || content.contains("we cannot process your request right now")
                                || content.contains("incident id:");

                if (detected) {
                        log.warn("Anti-bot markers detected in HTML content");
                }

                return detected;
        }

        private String readContent(InputStream inputStream) {

                try {
                        return new String(
                                        inputStream.readAllBytes(),
                                        StandardCharsets.UTF_8);
                } catch (IOException e) {
                        log.error(
                                        "Failed to read HTML input stream",
                                        e);

                        throw new HtmlDocumentExtractorException(
                                        "Failed to read HTML document",
                                        e);
                }
        }
}