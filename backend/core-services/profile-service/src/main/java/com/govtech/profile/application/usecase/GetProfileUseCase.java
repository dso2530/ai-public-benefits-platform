package com.govtech.profile.application.usecase;

import com.govtech.profile.api.dto.ProfileDto;

public interface GetProfileUseCase {

  ProfileDto getProfile(String subject);
}
