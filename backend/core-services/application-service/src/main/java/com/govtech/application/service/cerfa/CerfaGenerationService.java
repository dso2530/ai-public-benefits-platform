package com.govtech.application.service.cerfa;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.govtech.application.infrastructure.persistence.ApplicationJpaEntity;
import com.govtech.application.service.client.dto.ProfileContractDto;
import com.govtech.application.service.cerfa.model.CerfaTemplate;

@Service
@RequiredArgsConstructor
public class CerfaGenerationService {

        private final CerfaTemplateLoader templateLoader;
        private final PdfCerfaWriter writer;
        private final CerfaValueBuilder valueBuilder;

        public byte[] generate(
                        ApplicationJpaEntity application,
                        ProfileContractDto profile) {

                try {

                        CerfaTemplate template = templateLoader.load(
                                        getClass()
                                                        .getResourceAsStream(
                                                                        "/cerfa/10840-07-template.json"));

                        return writer.write(
                                        getClass()
                                                        .getResourceAsStream(
                                                                        "/cerfa/10840-07.pdf"),

                                        template,

                                        valueBuilder.build(
                                                        profile,
                                                        application));

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Cannot generate CERFA",
                                        e);
                }
        }
}