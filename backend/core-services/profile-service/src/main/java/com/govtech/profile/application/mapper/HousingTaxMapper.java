package com.govtech.profile.application.mapper;

import java.math.BigDecimal;

import com.govtech.events.housingtax.HousingTaxExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.HousingTax;
import com.govtech.profile.domain.valueobject.Identity;

public final class HousingTaxMapper {

        private HousingTaxMapper() {
        }

        public static UpdateProfileCommand toCommand(
                        HousingTaxExtractionCompletedEvent event) {

                var data = event.getExtractedData();

                return UpdateProfileCommand.builder()

                                .identity(new Identity(
                                                data.getTaxpayerFirstName(),
                                                data.getTaxpayerLastName(),
                                                null,
                                                null,
                                                null))

                                .address(new Address(
                                                data.getStreet(),
                                                data.getPostalCode(),
                                                data.getCity(),
                                                "FRANCE"))

                                .housingTax(new HousingTax(
                                                Integer.valueOf(data.getTaxYear()), // occupancyStatus
                                                null,
                                                new BigDecimal(data.getTaxAmount())))

                                .build();
        }
}