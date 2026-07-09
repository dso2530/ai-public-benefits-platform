package com.govtech.eligibility.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.eligibility.application.usecase.CheckEligibilityService;
import com.govtech.events.profile.ProfileUpdatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileUpdatedListener {

    private final CheckEligibilityService checkEligibilityService;

    @KafkaListener(topics = "profile.updated", groupId = "${messaging.kafka.group-id}")
    public void consume(ProfileUpdatedEvent event) {

        log.info(
                "Profile updated {}",
                event.getMetadata().getSubject());

        checkEligibilityService.check(
                event.getMetadata().getSubject());
    }
}