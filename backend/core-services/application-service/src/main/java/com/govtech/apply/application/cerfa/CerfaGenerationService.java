package com.govtech.apply.application.cerfa;

import org.springframework.stereotype.Service;

import com.govtech.apply.application.cerfa.model.CerfaTemplate;
import com.govtech.apply.application.client.dto.ProfileContractDto;
import com.govtech.apply.domain.model.Application;
import com.govtech.apply.infrastructure.persistence.ApplicationJpaMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CerfaGenerationService {

        private final CerfaTemplateLoader templateLoader;
        private final PdfCerfaWriter writer;
        private final CerfaValueBuilder valueBuilder;

        private final ApplicationJpaMapper applicationJpaMapper;

        public byte[] generate(
                        Application application,
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
                                                        applicationJpaMapper.toJpaEntity(application)));

                } catch (Exception e) {

                        throw new RuntimeException(
                                        "Cannot generate CERFA",
                                        e);
                }
        }
}