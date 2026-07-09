package com.govtech.eligibility.domain.repository;

import java.util.List;

import com.govtech.eligibility.domain.model.Eligibility;

public interface EligibilityRepository {

    void saveAll(List<Eligibility> eligibilities);

    List<Eligibility> findLatestBySubject(String subject);
}