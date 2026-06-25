package com.govtech.profile.application.usecase;

import com.govtech.profile.application.dto.ProfileDto;

public interface GetProfileUseCase {

  ProfileDto getProfile(String subject);
}
