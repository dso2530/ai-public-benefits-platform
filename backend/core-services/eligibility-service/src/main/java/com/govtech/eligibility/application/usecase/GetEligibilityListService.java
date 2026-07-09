package com.govtech.eligibility.application.usecase;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

import com.govtech.eligibility.api.dto.EligibilityDto;
import com.govtech.eligibility.domain.repository.EligibilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetEligibilityListService {

    private final EligibilityRepository eligibilityRepository;

    public List<EligibilityDto> execute(String subject) {

        return eligibilityRepository.findLatestBySubject(subject)
                .stream()
                .map(EligibilityDto::from)
                .toList();
    }
}