package com.govtech.profile.application.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.govtech.events.lease.LeaseExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.HousingInformation;
import com.govtech.profile.domain.valueobject.Lease;

public final class LeaseMapper {

        private LeaseMapper() {
        }

        public static UpdateProfileCommand toCommand(
                        LeaseExtractionCompletedEvent event) {

                var data = event.getExtractedData();

                return UpdateProfileCommand.builder()

                                .address(new Address(
                                                data.getStreet(),
                                                data.getPostalCode(),
                                                data.getCity(),
                                                "FRANCE"))

                                .housing(new HousingInformation(
                                                null,
                                                data.getHousingType(),
                                                null, // householdSize
                                                data.getSurface(),
                                                null, // monthlyRent
                                                null, // monthlyCharges
                                                null // aplRecipient
                                )).lease(

                                                new Lease(
                                                                data.getLandlordName(),
                                                                data.getMonthlyRent() == null
                                                                                ? null
                                                                                : BigDecimal.valueOf(
                                                                                                data.getMonthlyRent()),
                                                                parseDate(data.getEntryDate())))

                                .build();
        }

        private static LocalDate parseDate(String value) {
                return value == null || value.isBlank()
                                ? null
                                : LocalDate.parse(value);
        }
}