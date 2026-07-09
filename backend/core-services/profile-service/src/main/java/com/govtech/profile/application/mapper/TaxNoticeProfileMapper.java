package com.govtech.profile.application.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.govtech.events.tax.TaxNoticeExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.BankAccount;
import com.govtech.profile.domain.valueobject.TaxInformation;

public final class TaxNoticeProfileMapper {

    private TaxNoticeProfileMapper() {
    }

    public static UpdateProfileCommand toCommand(
            TaxNoticeExtractionCompletedEvent event) {

        var data = event.getExtractedData();

        return UpdateProfileCommand.builder()
                .address(new Address(
                        data.getStreet(),
                        data.getPostalCode(),
                        data.getCity(), "FRANCE"))
                .taxInformation(new TaxInformation(
                        data.getTaxpayer1().getTaxNumber(),
                        data.getTaxYear(),
                        data.getAssessmentYear(),
                        data.getReferenceIncome(),
                        data.getNumberOfShares() == null
                                ? null
                                : BigDecimal.valueOf(data.getNumberOfShares()),
                        data.getIncomeTax(),
                        data.getRefundAmount(),
                        data.getAmountToPay(),
                        parseDate(data.getRefundDate())))
                .bankAccount(new BankAccount(
                        data.getBankIban(),
                        data.getBankBic()))
                .build();
    }

    private static LocalDate parseDate(String value) {
        return value == null || value.isBlank()
                ? null
                : LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

}