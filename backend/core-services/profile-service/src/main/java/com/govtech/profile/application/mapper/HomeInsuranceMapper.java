package com.govtech.profile.application.mapper;

import java.time.LocalDate;

import com.govtech.events.homeinsurance.HomeInsuranceExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.HomeInsurance;

public final class HomeInsuranceMapper {

        private HomeInsuranceMapper() {
        }

        public static UpdateProfileCommand toCommand(
                        HomeInsuranceExtractionCompletedEvent event) {

                var data = event.getExtractedData();

                return UpdateProfileCommand.builder()

                                .address(new Address(
                                                data.getStreet(),
                                                data.getPostalCode(),
                                                data.getCity(),
                                                "FRANCE"))

                                .homeInsurance(new HomeInsurance(
                                                data.getInsuranceCompany(),
                                                data.getPolicyNumber(),
                                                null,
                                                LocalDate.parse(data.getEndDate())))

                                .build();
        }
}