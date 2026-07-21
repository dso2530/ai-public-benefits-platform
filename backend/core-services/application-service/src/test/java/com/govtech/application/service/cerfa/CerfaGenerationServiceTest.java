package com.govtech.application.service.cerfa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.govtech.application.service.client.dto.ProfileContractDto;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CerfaGenerationServiceTest.TestConfig.class)
class CerfaGenerationServiceTest {

    private final CerfaGenerationService service;

    @Autowired
    CerfaGenerationServiceTest(
            CerfaGenerationService service) {

        this.service = service;
    }

    @Test
    void should_generate_cerfa_pdf() throws Exception {

        ProfileContractDto profile = new ProfileContractDto(
                "subject",
                "marie@test.fr",
                "Marie",
                "DUPONT",
                "FR",
                LocalDate.of(1990, 3, 15),
                "18 rue des lillas",
                "75011",
                "PARIS",
                "LOCATAIRE",
                0,
                false,
                "30000",
                2025,
                2025,
                "290037510824567");

        byte[] generated = service.generate(null, profile);

        assertThat(generated)
                .isNotEmpty();

        Path generatedPdf = Path.of(
                "target/test-output/generated.pdf");

        Files.createDirectories(
                generatedPdf.getParent());

        Files.write(
                generatedPdf,
                generated);

        Path expected = Path.of(
                getClass()
                        .getResource("/cerfa/reference.pdf")
                        .toURI());

        Path actual = Path.of("target/test-output/generated.pdf");

        assertThat(PdfVisualComparator.assertSimilar(
                Files.readAllBytes(expected),
                Files.readAllBytes(actual))).isTrue();

    }

    @Configuration
    static class TestConfig {

        @Bean
        ObjectMapper objectMapper() {

            return JsonMapper.builder()
                    .configure(
                            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                            false)
                    .build();

        }

        @Bean
        CerfaTemplateLoader cerfaTemplateLoader(
                ObjectMapper mapper) {

            return new CerfaTemplateLoader(mapper);
        }

        @Bean
        PdfCerfaWriter pdfCerfaWriter() {

            return new PdfCerfaWriter();
        }

        @Bean
        CerfaValueBuilder cerfaValueBuilder() {

            return new CerfaValueBuilder();
        }

        @Bean
        CerfaGenerationService cerfaGenerationService(
                CerfaTemplateLoader loader,
                PdfCerfaWriter writer,
                CerfaValueBuilder builder) {

            return new CerfaGenerationService(
                    loader,
                    writer,
                    builder);
        }
    }
}