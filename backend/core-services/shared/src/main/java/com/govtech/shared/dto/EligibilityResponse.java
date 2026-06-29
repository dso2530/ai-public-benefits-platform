package com.govtech.shared.dto;

import java.util.List;

public record EligibilityResponse(

        boolean eligible,
        List<String> benefits

) {
}