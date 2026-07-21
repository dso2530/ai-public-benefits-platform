package com.govtech.profile.application.mapper;

import java.time.LocalDate;

import com.govtech.events.identity.IdentityCardExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.Identity;

public final class IdentityCardProfileMapper {

        private IdentityCardProfileMapper() {
        }

        public static UpdateProfileCommand toCommand(
                        IdentityCardExtractionCompletedEvent event) {

                var data = event.getExtractedData();

                return UpdateProfileCommand.builder()
                                .identity(new Identity(
                                                data.getFirstName(),
                                                data.getLastName(),
                                                LocalDate.parse(data.getBirthDate()),
                                                data.getBirthPlace(),
                                                data.getNationality()))
                                .build();
        }
}