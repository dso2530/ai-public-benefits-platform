package com.govtech.profile.application.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.govtech.events.caf.CafCertificateExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.CafInformation;

public final class CafInformationMapper {

        private CafInformationMapper() {
        }

        public static UpdateProfileCommand toCommand(
                        CafCertificateExtractionCompletedEvent event) {

                var data = event.getExtractedData();

                return UpdateProfileCommand.builder()
                                .cafInformation(new CafInformation(
                                                data.getBeneficiaryNumber(),
                                                data.getDepartmentCode(),
                                                data.getReceivesFamilyBenefits(),
                                                data.getReceivesHousingBenefits(),
                                                data.getReceivesRsa(),
                                                data.getReceivesAah(),
                                                data.getReceivesPrimeActivite(),
                                                data.getMonthlyAmount() == null
                                                                ? null
                                                                : BigDecimal.valueOf(data.getMonthlyAmount()),
                                                parseDate(data.getRightsStartDate()),
                                                parseDate(data.getRightsEndDate())))
                                .build();
        }

        private static LocalDate parseDate(String value) {
                return value == null || value.isBlank()
                                ? null
                                : LocalDate.parse(value);
        }
}