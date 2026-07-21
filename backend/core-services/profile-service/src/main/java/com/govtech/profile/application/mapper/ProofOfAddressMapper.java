package com.govtech.profile.application.mapper;

import com.govtech.events.proofofaddress.ProofOfAddressExtractionCompletedEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.Identity;

public final class ProofOfAddressMapper {

        private ProofOfAddressMapper() {
        }

        public static UpdateProfileCommand toCommand(
                        ProofOfAddressExtractionCompletedEvent event) {

                var data = event.getExtractedData();

                return UpdateProfileCommand.builder()
                                .identity(new Identity(
                                                data.getHolderFirstName(),
                                                data.getHolderLastName(),
                                                null,
                                                null,
                                                null))
                                .address(new Address(
                                                data.getStreet(),
                                                data.getPostalCode(),
                                                data.getCity(),
                                                data.getCountry()))
                                .build();
        }
}