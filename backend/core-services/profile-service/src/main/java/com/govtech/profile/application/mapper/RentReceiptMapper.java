package com.govtech.profile.application.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.govtech.events.rentreceipt.RentReceiptExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.RentReceipt;

public final class RentReceiptMapper {

        private RentReceiptMapper() {
        }

        public static UpdateProfileCommand toCommand(
                        RentReceiptExtractionCompletedEvent event) {

                var data = event.getExtractedData();

                return UpdateProfileCommand.builder()

                                .rentReceipt(new RentReceipt(
                                                data.getLandlordName(),
                                                data.getMonthlyRent() == null
                                                                ? null
                                                                : BigDecimal.valueOf(data.getMonthlyRent()),
                                                parseDate(data.getReceiptMonth())))

                                .build();
        }

        private static LocalDate parseDate(String value) {
                return value == null || value.isBlank()
                                ? null
                                : LocalDate.parse(value);
        }
}