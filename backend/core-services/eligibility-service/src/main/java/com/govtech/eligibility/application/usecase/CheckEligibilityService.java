package com.govtech.eligibility.application.usecase;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.eligibility.application.client.ProfileClient;
import com.govtech.eligibility.application.dto.ProfileContractDto;
import com.govtech.eligibility.application.mapper.EligibilityCheckedEventMapper;
import com.govtech.eligibility.domain.engine.EligibilityEngine;
import com.govtech.eligibility.domain.exception.EligibilityException;
import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.eligibility.domain.repository.EligibilityRepository;
import com.govtech.eligibility.infrastructure.client.exception.ProfileClientException;
import com.govtech.eligibility.infrastructure.persistence.exception.EligibilityPersistenceException;
import com.govtech.platform.messaging.publisher.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CheckEligibilityService {

    private final ProfileClient profileClient;
    private final EligibilityEngine engine;
    private final EligibilityRepository repository;
    private final EventPublisher publisher;

    public void check(String subject) {

        log.info("Starting eligibility calculation for subject={}", subject);

        try {
            ProfileContractDto profile = profileClient.getProfile(subject);

            log.debug("Profile successfully retrieved for subject={}", subject);

            UUID calculationId = UUID.randomUUID();
            Instant calculatedAt = Instant.now();

            List<Eligibility> results = engine.evaluate(
                    profile,
                    calculationId,
                    calculatedAt);

            log.info(
                    "Eligibility engine returned {} result(s) for subject={}",
                    results.size(),
                    subject);

            repository.saveAll(results);

            log.info(
                    "Persisted {} eligibility result(s) for calculationId={}",
                    results.size(),
                    calculationId);

            publisher.publish(
                    "eligibility.checked",
                    subject,
                    EligibilityCheckedEventMapper.toEvent(subject, results));

            log.info(
                    "Published eligibility.checked event for subject={} calculationId={}",
                    subject,
                    calculationId);

        } catch (EligibilityPersistenceException ex) {

            log.warn("Profile not found for subject={}", subject, ex);
            throw ex;

        } catch (ProfileClientException ex) {

            log.error("Unable to retrieve profile for subject={}", subject, ex);
            throw ex;

        } catch (Exception ex) {

            log.error("Eligibility calculation failed for subject={}", subject, ex);
            throw new EligibilityException(
                    "Unable to calculate eligibility for subject " + subject,
                    ex);
        }
    }
}