package com.govtech.eligibility.application.usecase;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.govtech.eligibility.application.dto.EligibilitySummaryDto;

@Service
public class GetEligibilitySummaryService {

    public EligibilitySummaryDto execute() {

        return new EligibilitySummaryDto(
                7,
                2,
                BigDecimal.valueOf(4280)
        );
    }
}