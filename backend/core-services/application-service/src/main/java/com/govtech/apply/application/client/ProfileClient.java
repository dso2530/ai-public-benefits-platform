package com.govtech.apply.application.client;

import com.govtech.apply.application.client.dto.ProfileContractDto;

public interface ProfileClient {

    ProfileContractDto getProfile(String subject);
}