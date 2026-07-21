package com.govtech.profile.application.mapper;

import com.govtech.events.bank.RibExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.BankAccount;

public final class BankAccountMapper {

        private BankAccountMapper() {
        }

        public static UpdateProfileCommand toCommand(
                        RibExtractionCompletedEvent event) {

                var data = event.getExtractedData();

                return UpdateProfileCommand.builder()
                                .bankAccount(new BankAccount(
                                                data.getIban(),
                                                data.getBic()))
                                .build();
        }
}