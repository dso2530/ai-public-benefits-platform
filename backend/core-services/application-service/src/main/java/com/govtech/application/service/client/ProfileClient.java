package com.govtech.application.service.client;

import com.govtech.application.service.client.dto.ProfileContractDto;

public interface ProfileClient {

    ProfileContractDto getProfile(String subject);
}