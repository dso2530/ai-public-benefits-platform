package com.govtech.eligibility.application.client;

import com.govtech.eligibility.application.dto.ProfileContractDto;

public interface ProfileClient {

    ProfileContractDto getProfile(String subject);
}