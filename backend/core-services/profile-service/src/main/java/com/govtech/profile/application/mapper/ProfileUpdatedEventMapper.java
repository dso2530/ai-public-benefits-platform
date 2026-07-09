package com.govtech.profile.application.mapper;

import java.time.Instant;
import java.util.UUID;

import com.govtech.events.common.BaseEvent;
import com.govtech.events.profile.ProfileUpdatedEvent;
import com.govtech.profile.domain.model.Citizen;

public final class ProfileUpdatedEventMapper {

    private ProfileUpdatedEventMapper() {
    }

    public static ProfileUpdatedEvent toEvent(Citizen citizen) {
        return ProfileUpdatedEvent.newBuilder()
                .setMetadata(
                        BaseEvent.newBuilder()

                                .setEventId(UUID.randomUUID().toString())
                                .setOccurredAt(Instant.now().toString())
                                .setSubject(citizen.getSubject())
                                .build())
                .build();
    }
}