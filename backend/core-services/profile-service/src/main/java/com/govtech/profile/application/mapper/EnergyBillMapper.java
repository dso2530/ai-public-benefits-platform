package com.govtech.profile.application.mapper;

import java.time.LocalDate;

import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.EnergyBill;

public final class EnergyBillMapper {

    private EnergyBillMapper() {
    }

    public static UpdateProfileCommand toCommand(
            com.govtech.events.energy.EnergyBillExtractionCompletedEvent event) {

        var data = event.getExtractedData();

        return UpdateProfileCommand.builder()

                .address(new Address(
                        data.getStreet(),
                        data.getPostalCode(),
                        data.getCity(),
                        data.getCountry()))

                .energyBill(new EnergyBill(
                        data.getProvider(),
                        data.getContractReference(),
                        parseDate(data.getDocumentDate())))

                .build();
    }

    private static LocalDate parseDate(String value) {
        return value == null || value.isBlank()
                ? null
                : LocalDate.parse(value);
    }
}